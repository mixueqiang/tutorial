package com.yike.web.api.v1;

import com.yike.Constants;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.Entity;
import com.yike.model.User;
import com.yike.util.RandomUtil;
import com.yike.util.ResponseBuilder;
import com.yike.web.BaseResource;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author mixueqiang
 * @since Sep 12, 2016
 */
@Path("/api/v1/user")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiUserResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiUserResource.class);
  private static final String[] PROFILE_KEYS = new String[]{"username", "avatar", "gender", "birthday", "profile"};

  @GET
  @Path("{id}")
  @Produces(APPLICATION_JSON)
  public Map<String, Object> get(@PathParam("id") long userId) {
    User user = entityDao.get("user", userId, UserRowMapper.getInstance());
    if (user == null || user.isDisabled()) {
      return ResponseBuilder.error(10404, "未找到用户。");
    }

    Map<String, Object> result = new HashMap<String, Object>();
    result.put("id", user.getId());
    result.put("username", user.getUsername());
    result.put("profile", user.getProfile());

    return ResponseBuilder.ok(result);
  }

  @GET
  @Produces(APPLICATION_JSON)
  public Map<String, Object> get() {
    User user = getSessionUser();
    if (user == null) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    user = entityDao.get("user", user.getId(), UserRowMapper.getInstance());
    if (user == null || user.isDisabled()) {
      return ResponseBuilder.error(10404, "未找到用户。");
    }

    Map<String, Object> result = new HashMap<String, Object>();
    result.put("id", user.getId());
    result.put("email", user.getEmail());
    result.put("phone", user.getPhone());
    result.put("username", user.getUsername());
    result.put("avatar", user.getAvatar());
    result.put("gender", user.getGender());
    result.put("birthday", user.getBirthday());
    result.put("profile", user.getProfile());
    result.put("roles", user.getRoles());

    return ResponseBuilder.ok(result);
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> save(@FormParam("email") String email, @FormParam("phone") String phone, @FormParam("securityCode") String securityCode, @FormParam("username") String username,
                                  @FormParam("password") String password, @FormParam("role") String role) {
    // Data validation.
    if (StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
      return ResponseBuilder.error(10100, "请输入邮箱或手机号码。");
    }
    if (StringUtils.isNotEmpty(email) && !email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
      return ResponseBuilder.error(10102, "请输入有效的邮箱。");

    } else if (StringUtils.isNotEmpty(phone) && StringUtils.length(phone) != 11) {
      return ResponseBuilder.error(10104, "请输入有效的手机号码。");
    }
    if (StringUtils.isEmpty(securityCode)) {
      return ResponseBuilder.error(10105, "请输入验证码。");
    }
    if (StringUtils.isEmpty(password)) {
      return ResponseBuilder.error(10108, "请输入密码。");
    }
    if (StringUtils.isEmpty(username)) {
      return ResponseBuilder.error(10106, "昵称不能少于2个字，支持中文、字母、数字和下划线。");
    }
    String regex = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{2,20}$";
    Pattern pattern = Pattern.compile(regex);
    if (!pattern.matcher(username).matches()) {
      return ResponseBuilder.error(10107, "昵称不能少于2个字，支持中文、字母、数字和下划线。");
    }
    String roles = "user";
    if (StringUtils.isNotEmpty(role)) {
      if (!StringUtils.equals("worker", role) && !StringUtils.equals("company", role) && !StringUtils.equals("client", role)) {
        return ResponseBuilder.error(10108, "请选择用户类型。");
      }
      roles += "," + role;
    }

    try {
      if (StringUtils.isNotEmpty(email)) {
        if (entityDao.exists("user", "email", email)) {
          return ResponseBuilder.error(10110, "你的邮箱已经注册过，可以直接登录。");
        }

      } else {
        if (entityDao.exists("user", "phone", phone)) {
          return ResponseBuilder.error(10111, "你的手机号码已经注册过，可以直接登录。");
        }
      }

      if (entityDao.exists("user", "username", username)) {
        return ResponseBuilder.error(10113, "昵称已经被使用，请重新选择一个昵称。");
      }

      Map<String, Object> condition = new HashMap<String, Object>();
      condition.put("type", 1);
      if (StringUtils.isNotEmpty(email)) {
        condition.put("email", email);

      } else {
        condition.put("phone", phone);
      }
      condition.put("status", 1);
      Entity securityCodeEntity = entityDao.findOne("security_code", condition);
      if (securityCodeEntity == null || !StringUtils.equals(securityCode, securityCodeEntity.getString("securityCode"))) {
        return ResponseBuilder.error(10114, "无效的验证码。");
      }

      long time = System.currentTimeMillis();
      // Save user.
      Entity entity = new Entity("user");
      entity.set("email", email).set("phone", phone);
      if (StringUtils.isEmpty(username)) {
        entity.set("username", RandomUtil.randomString(6) + System.currentTimeMillis());

      } else {
        entity.set("username", username);
      }
      entity.set("password", password);
      entity.set("locale", "cn").set("roles", roles);
      entity.set("status", Constants.STATUS_ENABLED).set("createTime", time);
      entity = entityDao.saveAndReturn(entity);

      entityDao.update("security_code", "id", securityCodeEntity.getId(), "status", 0);

      // 自动登录
      long userId = entity.getId();
      User user = entityDao.get("user", userId, UserRowMapper.getInstance());
      // 更新并保存session
      setSessionAttribute("_user", user);
      sessionService.storeSession(userId, WebUtils.getSessionId(request));
      entityDao.update("user", "id", userId, "loginTime", time);

      return ResponseBuilder.OK;

    } catch (Throwable t) {
      LOG.error("Failed to register user.", t);
      return ResponseBuilder.error(50000, "注册失败，请稍后再试！");
    }
  }

  @POST
  @Path("signin")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> signin(@FormParam("id") String id, @FormParam("password") String password, @FormParam("to") String to) {
    if (StringUtils.isEmpty(id)) {
      return ResponseBuilder.error(10301, "帐号不可以为空。");
    }
    if (StringUtils.isEmpty(password)) {
      return ResponseBuilder.error(10302, "密码不可以为空。");
    }

    User user = null;
    if (StringUtils.contains(id, "@")) {
      user = entityDao.findOne("user", "email", id, UserRowMapper.getInstance());

    } else if (StringUtils.length(id) == 11) {
      user = entityDao.findOne("user", "phone", id, UserRowMapper.getInstance());

    } else {
      return ResponseBuilder.error(10303, "请输入有效的手机号码或邮箱。");
    }

    if (user == null) {
      LOG.warn("User [" + id + "] signed in failed!");
      return ResponseBuilder.error(10303, "帐号或密码错误。");
    }
    if (user.isDisabled()) {
      return ResponseBuilder.error(10304, "帐号已被禁用，请联系管理员处理。");
    }

    long userId = user.getId();
    long time = System.currentTimeMillis();
    if (StringUtils.equals(password, user.getPassword())) {
      // 更新并保存session
      setSessionAttribute("_user", user);
      sessionService.storeSession(userId, WebUtils.getSessionId(request));
      entityDao.update("user", "id", userId, "loginTime", time);

      if (StringUtils.isEmpty(to)) {
        to = "/dashboard";
      }

      return ResponseBuilder.ok(to);
    }

    LOG.warn("User [" + id + "] signed in failed!");
    return ResponseBuilder.error(10303, "帐号或密码错误。");
  }

  @GET
  @Path("signout")
  @Produces(APPLICATION_JSON)
  public Map<String, Object> signout() {
    try {
      // Clear sessionId.
      String sessionId = WebUtils.getSessionId(request);
      if (StringUtils.isNotEmpty(sessionId)) {
        sessionService.destorySession(sessionId);
      }

      request.getSession().invalidate();
      return ResponseBuilder.OK;

    } catch (Throwable t) {
      LOG.error("User can not sign out.", t);
      return ResponseBuilder.error(10302, "退出登录失败。");
    }
  }

  @POST
  @Path("update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> updateProfile(@FormParam("name") String name, @FormParam("value") String value) {
    User user = getSessionUser();
    if (user == null) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    if (!ArrayUtils.contains(PROFILE_KEYS, name)) {
      return ResponseBuilder.error(10201, "不支持的用户资料信息。");
    }

    if (StringUtils.equals("username", name)) {
      if (StringUtils.isEmpty(value)) {
        return ResponseBuilder.error(10109, "用户昵称不能为空。");
      }

      if (StringUtils.length(value) < 2) {
        return ResponseBuilder.error(10110, "用户昵称不能少于2个字。");
      }

      if (StringUtils.length(value) > 10) {
        return ResponseBuilder.error(10111, "用户昵称不能超过10个字。");
      }

      String regex = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{2,10}$";
      Pattern pattern = Pattern.compile(regex);
      if (!pattern.matcher(value).matches()) {
        return ResponseBuilder.error(10119, "用户昵称只能使用中文、字母、数字和下划线，不能少于2个字。");
      }

      if (entityDao.exists("user", "username", value)) {
        return ResponseBuilder.error(10120, "用户昵称已经被使用。");
      }

    } else if (StringUtils.equals("avatar", name)) {
      if (StringUtils.isEmpty(value)) {
        return ResponseBuilder.error(10111, "用户头像不能设置为空。");
      }

    } else if (StringUtils.equals("gender", name)) {
      if (StringUtils.isEmpty(value)) {
        return ResponseBuilder.error(10112, "性别不能设置为空。");
      }
      if (StringUtils.length(value) > 1) {
        return ResponseBuilder.error(10113, "性别数据不合法，请重新选择设置。");
      }

    } else if (StringUtils.equals("birthday", name)) {
      if (StringUtils.isEmpty(value)) {
        return ResponseBuilder.error(10114, "生日不能设置为空。");
      }
      if (StringUtils.length(value) > 10) {
        return ResponseBuilder.error(10115, "生日数据不合法，请重新选择设置。");
      }

    } else if (StringUtils.equals("profile", name)) {
      if (StringUtils.length(value) > 100) {
        return ResponseBuilder.error(10118, "用户签名不能超过100个字。");
      }

    } else {
      return ResponseBuilder.error(10201, "不支持的用户资料信息。");
    }

    Map<String, Object> updateValues = new HashMap<String, Object>();
    updateValues.put(name, value);
    updateValues.put("updateTime", System.currentTimeMillis());
    entityDao.update("user", "id", user.getId(), updateValues);

    // 更新Session 缓存。
    User newUser = entityDao.get("user", user.getId(), UserRowMapper.getInstance());
    WebUtils.setSessionAttribute(request, "_user", newUser);
    return ResponseBuilder.OK;
  }

}
