package com.yike.web.api.v1;

import com.yike.Constants;
import com.yike.dao.mapper.UserOauthRowMapper;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.*;
import com.yike.util.RandomUtil;
import com.yike.util.ResponseBuilder;
import com.yike.web.BaseResource;
import com.yike.web.util.QQAuthUtils;
import com.yike.web.util.WechatAuthUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/4/7
 */

@Path("api/v1/auth")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiAuthResource extends BaseResource {
    private static Log LOG = LogFactory.getLog(ApiAuthResource.class);

    @POST
    @Path("request")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Map<String, Object> request(@FormParam("type") String type) {
        if ("qq".equals(type)) {
            return ResponseBuilder.ok(QQAuthUtils.getAuthUrl());
        }
        if ("wechat".equals(type)) {
            return ResponseBuilder.ok(WechatAuthUtils.getAuthUrl());
        }
        return ResponseBuilder.error(50000, "参数不正确");
    }

    @GET
    @Path("qq")
    @Produces(APPLICATION_JSON)
    public Response qq(@QueryParam("code") String code,
                       @QueryParam("usercancel") int userCancel) {
        LOG.info("Get /api/v1/auth/qq?code=" + code);
        if (StringUtils.isEmpty(code)) {
            LOG.error("QQ response a empty code");
            return redirect("/signin");
        }

        String accessToken = QQAuthUtils.requestAccessTokenByCode(code);
        if (StringUtils.isEmpty(accessToken)) {
            LOG.error("QQ response a empty accessToken");
            return redirect("/signin");
        }

        String openId = QQAuthUtils.getOpenId(accessToken);
        if (StringUtils.isEmpty(openId)) {
            LOG.error("QQ response a empty openId");
            return redirect("/signin");
        }

        UserOauth oauth = getQQOauth(openId);
        if (signinWithUserOauth(oauth)) {
            LOG.info("查询到已存在的qq授权信息。登录成功");
            return redirect("/");
        }

        LOG.info("未查询到已存在的qq授权信息，或者用户信息未绑定，登录失败。开始请求用户信息：");

        QQUserInfo userInfo = QQAuthUtils.getUserInfo(accessToken, openId);
        long newUserId = saveQQUserInfo(userInfo, openId);
        oauth = saveQQOauth(accessToken, newUserId, openId);
        if (signinWithUserOauth(oauth)) {
            LOG.info("查询到已存在的qq授权信息。登录成功");
            return redirect("/");
        }

        return redirect("/signin");
    }

    @GET
    @Path("wechat")
    @Produces(APPLICATION_JSON)
    public Response wx(@QueryParam("code") String code,
                       @QueryParam("state") String state) {
        LOG.info("Get /api/v1/auth/wechat?code=" + code + "&state=" + state);
        if (StringUtils.isEmpty(code)) {
            // 用户禁止授权。
            LOG.error("用户禁止微信授权");
            return redirect("/signin");
        }
        AccessTokenContainer accessTokenContainer = WechatAuthUtils.getAccessTokenByCode(code);
        if (accessTokenContainer == null) {
            LOG.error("请求微信 accessToken 失败");
            return redirect("/signin");
        }

        UserOauth userOauth = getWechatOauth(accessTokenContainer.getUnionId());
        if (signinWithUserOauth(userOauth)) {
            return redirect("/");
        }
        LOG.info("收据库中未找到授权信息，开始拉取用户信息准备保存");

        WechatUserInfo info = WechatAuthUtils.getUserInfo(accessTokenContainer);
        if (info == null) {
            LOG.error("微信登录失败，获得用户信息失败");
            return redirect("/signin");
        }

        long newUserId = saveWechatUserInfo(info);
        userOauth = saveWechatOauth(accessTokenContainer, newUserId);
        if (signinWithUserOauth(userOauth)) {
            return redirect("/");
        }
        LOG.error("微信登录失败");
        return redirect("/signin");
    }

    private boolean signinWithUserOauth(UserOauth userOauth) {
        if (userOauth == null) {
            return false;
        }
        long userId = userOauth.getUserId();
        if (userId == 0) {
            return false;
        }
        try {
            User user = entityDao.get("user", userId, UserRowMapper.getInstance());
            setSessionAttribute("_user", user);
            sessionService.storeSession(user.getId(), WebUtils.getSessionId(request));
            entityDao.update("user", "id", user.getId(), "loginTime", System.currentTimeMillis());
            return true;
        } catch (Throwable t) {
            LOG.error("Sign in with user oauth failure", t);
        }
        return false;
    }

    private UserOauth getWechatOauth(String unionId) {
        if (StringUtils.isEmpty(unionId)) {
            return null;
        }
        try {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("status", Constants.STATUS_OK);
            condition.put("unionId", unionId);
            condition.put("provider", UserOauth.PROVIDER_WECHAT);
            return entityDao.findOne("user_oauth", condition, UserOauthRowMapper.getInstance());
        } catch (Throwable t) {
            LOG.error("Find user oauth exists failure", t);
            return null;
        }
    }

    private UserOauth getQQOauth(String openId) {
        if (StringUtils.isEmpty(openId)) {
            return null;
        }
        try {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("status", Constants.STATUS_OK);
            condition.put("openId", openId);
            condition.put("provider", UserOauth.PROVIDER_QQ);
            return entityDao.findOne("user_oauth", condition, UserOauthRowMapper.getInstance());
        } catch (Throwable t) {
            LOG.error("Find user oauth exists failure", t);
            return null;
        }
    }

    private UserOauth saveWechatOauth(AccessTokenContainer accessToken, long userId) {
        if (userId == 0) {
            return null;
        }
        if (StringUtils.isEmpty(accessToken.getOpenId())) {
            return null;
        }

        UserOauth userOauth = getWechatOauth(accessToken.getOpenId());
        if (userOauth != null) {
            if (userOauth.getUserId() != 0) {
                return userOauth;
            }
            try {
                entityDao.update("user_oauth", "unionId", accessToken.getUnionId(), "userId", userId);
                return userOauth;
            } catch (Throwable t) {
                LOG.error("Find exist user_oauth, but update userId failure", t);
            }
        }

        Entity entity = new Entity("user_oauth")
                .set("userId", userId)
                .set("openId", accessToken.getOpenId())
                .set("unionId", accessToken.getUnionId())
                .set("createTime", System.currentTimeMillis())
                .set("provider", UserOauth.PROVIDER_WECHAT)
                .set("status", Constants.STATUS_OK);
        long newId = 0;
        try {
            newId = entityDao.saveAndReturn(entity).getId();
        } catch (Throwable t) {
            LOG.error("Save user_oauth failure", t);
            return null;
        }
        if (newId == 0) {
            return null;
        }
        return entityDao.get("user_oauth", newId, UserOauthRowMapper.getInstance());

    }

    private long saveWechatUserInfo(WechatUserInfo info) {
        if (info == null) {
            return 0;
        }
        if (StringUtils.isEmpty(info.getOpenid())) {
            return 0;
        }
        String name = info.getNickname();
        if (StringUtils.isEmpty(name)) {
            name = RandomUtil.randomString(6) + System.currentTimeMillis();
        }
        Entity entity = new Entity("user")
                .set("name", name)
                .set("roles", "user")
                .set("status", Constants.STATUS_OK)
                .set("createTime", System.currentTimeMillis())
                .set("gender", info.getSex());
        if (StringUtils.isNotEmpty(info.getHeadimgurl())) {
            entity.set("avatar", info.getHeadimgurl());
        }
        try {
            return entityDao.saveAndReturn(entity).getId();
        } catch (Throwable t) {
            LOG.error("Save user failure", t);
            return 0;
        }
    }

    private UserOauth saveQQOauth(String accessToken, long userId, String openId) {
        if (userId == 0) {
            return null;
        }
        if (StringUtils.isEmpty(openId)) {
            return null;
        }

        UserOauth userOauth = getQQOauth(openId);
        if (userOauth != null) {
            if (userOauth.getUserId() != 0) {
                return userOauth;
            }
            try {
                entityDao.update("user_oauth", "openId", openId, "userId", userId);
                return userOauth;
            } catch (Throwable t) {
                LOG.error("Find exist user_oauth, but update userId failure", t);
            }
        }

        Entity entity = new Entity("user_oauth")
                .set("userId", userId)
                .set("openId", openId)
                .set("createTime", System.currentTimeMillis())
                .set("provider", UserOauth.PROVIDER_QQ)
                .set("status", Constants.STATUS_OK);
        long newId = 0;
        try {
            newId = entityDao.saveAndReturn(entity).getId();
        } catch (Throwable t) {
            LOG.error("Save user_oauth failure", t);
            return null;
        }
        if (newId == 0) {
            return null;
        }
        return entityDao.get("user_oauth", newId, UserOauthRowMapper.getInstance());

    }

    private long saveQQUserInfo(QQUserInfo info, String openId) {
        if (info == null) {
            return 0;
        }
        if (StringUtils.isEmpty(openId)) {
            return 0;
        }
        String name = info.getNickname();
        if (StringUtils.isEmpty(name)) {
            name = RandomUtil.randomString(6) + System.currentTimeMillis();
        }
        int gender = "男".equals(info.getGender()) ? 1 : 2;
        Entity entity = new Entity("user")
                .set("name", name)
                .set("roles", "user")
                .set("status", Constants.STATUS_OK)
                .set("createTime", System.currentTimeMillis())
                .set("gender", gender);
        if (StringUtils.isNotEmpty(info.getFigureurl_qq_1())) {
            entity.set("avatar", info.getFigureurl_qq_1());
        }
        try {
            return entityDao.saveAndReturn(entity).getId();
        } catch (Throwable t) {
            LOG.error("Save user failure", t);
            return 0;
        }
    }
}
