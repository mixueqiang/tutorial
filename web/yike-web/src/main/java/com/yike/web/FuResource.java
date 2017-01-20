package com.yike.web;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.sun.jersey.api.view.Viewable;
import com.yike.Constants;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.Entity;
import com.yike.model.User;
import com.yike.util.Pair;
import com.yike.util.RandomUtil;
import com.yike.util.ResponseBuilder;
import com.yike.util.SmsUtilsYunpian;
import com.yike.web.api.v1.ApiUserResource;

/**
 * @author mixueqiang
 * @since Jan 19, 2017
 *
 */
@Path("/fu")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class FuResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiUserResource.class);
  public static final Map<String, Pair<String, String>> FU_COLLECTION = new HashMap<String, Pair<String, String>>();

  static {
    FU_COLLECTION.put("fu1", new Pair<String, String>("爱国福", "fu1.png"));
    FU_COLLECTION.put("fu2", new Pair<String, String>("富强福", "fu2.png"));
    FU_COLLECTION.put("fu3", new Pair<String, String>("和谐福", "fu3.png"));
    FU_COLLECTION.put("fu4", new Pair<String, String>("友善福", "fu4.png"));
    FU_COLLECTION.put("fu5", new Pair<String, String>("敬业福", "fu5.png"));
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    int count = entityDao.count("skill_exchange");
    request.setAttribute("count", count);

    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("exchange")
  @Produces(APPLICATION_JSON)
  public Map<String, Object> exchange(@QueryParam("id") long exchangeId) {
    User user = getSessionUser();
    if (user == null) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    Entity entity = entityDao.get("skill_exchange", exchangeId);
    return ResponseBuilder.ok(entity);
  }

  @POST
  @Path("publish")
  @Produces(APPLICATION_JSON)
  public Map<String, Object> publish(@FormParam("source") int source, @FormParam("target") int target, @FormParam("alipay") String alipay) {
    if (source <= 0 && target <= 0) {
      return ResponseBuilder.error(50000, "请选择你有的和需要的福。");
    }
    if (StringUtils.isEmpty(alipay)) {
      return ResponseBuilder.error(50000, "请输入支付宝账号。");
    }

    long time = System.currentTimeMillis();
    User user = getSessionUser();

    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put("source", source);
    condition.put("target", target);
    condition.put("alipay", alipay);
    if (entityDao.exists("skill_exchange", condition)) {
      return ResponseBuilder.error(50000, "已发布过同样的换福信息");
    }

    Entity entity = new Entity("skill_exchange");
    if (user != null) {
      entity.set("userId", user.getId()).set("username", user.getUsername());
    }
    entity.set("source", source).set("target", target).set("contact", alipay).set("alipay", alipay);
    entity.set("status", 1).set("createTime", time);
    entityDao.save(entity);

    entity = new Entity("skill_user");
    if (user != null) {
      entity.set("userId", user.getId()).set("username", user.getUsername());
    }
    entity.set("skillId", source).set("contact", alipay);
    entity.set("status", 1).set("createTime", time);
    entityDao.save(entity);

    return ResponseBuilder.OK;
  }

  @GET
  @Path("sms/send")
  @Produces(APPLICATION_JSON)
  public Map<String, Object> sendSms(@QueryParam("phone") String phone, @QueryParam("type") String type) {
    if (StringUtils.isEmpty(phone)) {
      return ResponseBuilder.error(10601, "请输入手机号码。");
    }

    if (!StringUtils.equals(type, "fu")) {
      return ResponseBuilder.error(10602, "未知类型的短信。");
    }

    long time = System.currentTimeMillis();
    // Avoid frequent sending requests.
    Long smsLastSentTime = (Long) getSessionAttribute("smsLastSentTime");
    if (smsLastSentTime != null && time - smsLastSentTime <= 60 * 1000) {
      return ResponseBuilder.error(10610, "60秒内不可以重复发送验证码！");
    }

    // Generate security code.
    String securityCode = RandomUtil.randomNumber(4);
    String[] datas = new String[] { securityCode };
    if (SmsUtilsYunpian.send(phone, type, datas)) {
      LOG.info("Send " + type + " sms to phone: " + phone);
      setSessionAttribute("smsLastSentTime", time);

      int businessType = 5;
      Entity entity = new Entity("security_code");
      entity.set("type", businessType).set("phone", phone);
      entity.set("securityCode", securityCode).set("sendTime", time);
      entity.set("status", 1).set("createTime", time);
      entityDao.save(entity);

      return ResponseBuilder.OK;

    } else {
      return ResponseBuilder.error(50000, "短信发送失败！");
    }
  }

  @POST
  @Path("user")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> signupForExchange(@FormParam("phone") String phone, @FormParam("securityCode") String securityCode, @FormParam("username") String username,
      @FormParam("password") String password, @FormParam("exchangeId") long exchangeId) {
    // Data validation.
    if (StringUtils.isEmpty(phone)) {
      return ResponseBuilder.error(10100, "请输入手机号。");
    }
    if (StringUtils.length(phone) != 11) {
      return ResponseBuilder.error(10104, "请输入有效的手机号。");
    }
    if (StringUtils.isEmpty(securityCode)) {
      return ResponseBuilder.error(10105, "请输入验证码。");
    }
    if (StringUtils.isEmpty(password)) {
      return ResponseBuilder.error(10108, "请输入密码。");
    }

    try {
      Map<String, Object> condition = new HashMap<String, Object>();
      condition.put("type", 5);
      condition.put("phone", phone);
      condition.put("status", 1);
      Entity securityCodeEntity = entityDao.findOne("security_code", condition);
      if (securityCodeEntity == null || !StringUtils.equals(securityCode, securityCodeEntity.getString("securityCode"))) {
        return ResponseBuilder.error(10114, "无效的验证码。");
      }

      long time = System.currentTimeMillis();
      // Save user.
      if (!entityDao.exists("user", "phone", phone)) {
        Entity userEntity = new Entity("user");
        userEntity.set("phone", phone);
        if (StringUtils.isEmpty(username)) {
          username = RandomUtil.randomString(3) + System.currentTimeMillis();
        }
        userEntity.set("username", username);
        userEntity.set("password", password);
        userEntity.set("locale", "cn").set("roles", "user");
        userEntity.set("status", Constants.STATUS_ENABLED).set("createTime", time);
        userEntity = entityDao.saveAndReturn(userEntity);
        entityDao.update("security_code", "id", securityCodeEntity.getId(), "status", 0);
      }

      // 自动登录
      User user = entityDao.findOne("user", "phone", phone, UserRowMapper.getInstance());
      // 更新并保存session
      setSessionAttribute("_user", user);
      long userId = user.getId();
      sessionService.storeSession(userId, WebUtils.getSessionId(request));
      entityDao.update("user", "id", userId, "loginTime", time);

      // 获得换福
      Entity entity = entityDao.get("skill_exchange", exchangeId);
      return ResponseBuilder.ok(entity);

    } catch (Throwable t) {
      LOG.error("Failed to register user.", t);
      return ResponseBuilder.error(50000, "访问失败，请稍后再试！");
    }
  }

}
