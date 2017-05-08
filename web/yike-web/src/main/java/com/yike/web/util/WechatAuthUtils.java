package com.yike.web.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yike.model.AccessTokenContainer;
import com.yike.model.WechatUserInfo;
import com.yike.util.HttpClientUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/3/24
 */
public class WechatAuthUtils {
    private static final Log LOG = LogFactory.getLog(WechatAuthUtils.class);

    private static final String APP_ID = "wxa0f76426b51092f2";
    private static final String APP_SECRET = "";
    private static final String REDIRECT_URI = "http%3A%2F%2Fwww.yikeshangshou.com%2Fapi%2Fv1%2Fauth%2Fwechat";

    public static String getAuthUrl() {
        return "https://open.weixin.qq.com/connect/qrconnect?appid=" + APP_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=code" +
                "&scope=" + "snsapi_login" +
                "&state=" + System.currentTimeMillis() +
                "#wechat_redirect";
    }

    /**
     * 通过微信返回的 code 请求 access_token
     *
     * @param code
     * @return
     */
    public static AccessTokenContainer getAccessTokenByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + APP_ID +
                "&secret=" + APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code";

        Response response = get(url);
        if (response == null || response.getErrcode() != 0) {
            return null;
        }
        return parseAccessToken(response.getSource());
    }

    /**
     * 通过 access_token 请求用户信息
     *
     * @param container
     * @return
     */
    public static WechatUserInfo getUserInfo(AccessTokenContainer container) {
        String token = container.getAccessToken();
        String openId = container.getOpenId();
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(openId)) {
            return null;
        }

        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + token +
                "&openid=" + openId;

        Response response = get(url);
        if (response == null || response.getErrcode() != 0) {
            return null;
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        WechatUserInfo userInfo = null;
        try {
            userInfo = gson.fromJson(response.getSource(), WechatUserInfo.class);
        } catch (Throwable t) {
            LOG.error("Encoding json to WechatUserInfo failure", t);
        }
        return userInfo;
    }


    /**
     * 通过 refresh_token 刷新 access_token，目前没有保存 access_token 与 refresh_token
     *
     * @param refreshToken
     * @return
     */
    public static AccessTokenContainer refreshAccessToken(String refreshToken) {
        if (StringUtils.isEmpty(refreshToken)) {
            return null;
        }

        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                "appid=" + APP_ID +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;

        Response response = get(url);
        if (response == null || response.getErrcode() != 0) {
            return null;
        }

        return parseAccessToken(response.getSource());
    }

    /**
     * 检查 access_token 是否过期
     *
     * @param token
     * @param openId
     * @return
     */
    public static boolean tokenIsExpired(String token, String openId) {
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(openId)) {
            return false;
        }
        String url = "https://api.weixin.qq.com/sns/auth?" +
                "access_token=" + token +
                "&openid=" + openId;
        Response response = get(url);
        return response == null || response.getErrcode() != 0;
    }

    private static AccessTokenContainer parseAccessToken(String result) {

        Gson gson = new Gson();
        AccessTokenContainer token = null;
        try {
            Map<String, String> json = gson.fromJson(result, new TypeToken<Map<String, String>>() {
            }.getType());
            token = new AccessTokenContainer();
            token.setAccessToken(json.get("access_token"));
            token.setRefreshToken(json.get("refresh_token"));
            token.setOpenId(json.get("openid"));
            token.setScope(json.get("scope"));
            token.setUnionId(json.get("unionid"));
            String exp = json.get("expires_in");
            token.setExpiresIn(Integer.parseInt(exp));
        } catch (Throwable t) {
            LOG.error("Encoding json string to AccessTokenContainer failure", t);
        }
        return token;
    }

    private static Response get(String url) {
        try {
            String result = HttpClientUtils.get(url);
            LOG.info("GET request to Wechat with url: \n【" + url + "】\nand response:\n【" + result + "】");
            if (StringUtils.isEmpty(result)) {
                return null;
            }
            Gson g = new GsonBuilder().disableHtmlEscaping().create();
            Response response = g.fromJson(result, Response.class);
            if (response != null) {
                response.setSource(result);
            }
            return response;
        } catch (Throwable t) {
            return null;
        }
    }
}

class Response {

    private int errcode;
    private String errmsg;
    private String source;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}