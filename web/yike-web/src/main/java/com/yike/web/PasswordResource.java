package com.yike.web;

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

import com.yike.web.util.SmsUtilsYunpian;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.Entity;
import com.yike.model.User;
import com.yike.util.RandomUtil;
import com.yike.util.ResponseBuilder;
import com.sun.jersey.api.view.Viewable;
//import com.yike.util.SmsUtilsYunpian;


/**
 * @author mixueqiang
 * @since Jun 2, 2014
 */
@Path("/password")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PasswordResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(PasswordResource.class);

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("remind")
  @Produces(MediaType.TEXT_HTML)
  public Response remind() {
    return Response.ok(new Viewable("remind")).build();
  }

  @POST
  @Path("remind")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> remind(@FormParam("email") String email, @FormParam("phone") String phone) {
    if (StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
      return ResponseBuilder.error(10700, "请输入邮箱或手机号。");
    }

    if (StringUtils.isNotEmpty(email) && !email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
      return ResponseBuilder.error(10701, "请输入有效的邮箱地址。");
    }
    if (StringUtils.isNotEmpty(phone) && StringUtils.length(phone) != 11) {
      return ResponseBuilder.error(10702, "请输入有效的手机号。");
    }

    long time = System.currentTimeMillis();
    if (StringUtils.isNotEmpty(email)) {
      LOG.info("User " + email + " submited a password remind request.");
      User user = entityDao.findOne("user", "email", email, UserRowMapper.getInstance());
      if (user == null) {
        return ResponseBuilder.error(10703, "您输入的邮箱尚未注册。");
      }

      if (user.isDisabled()) {
        return ResponseBuilder.error(10304, "帐号尚未激活，请先去注册邮箱激活你的帐号，然后再进行登录。");
      }

      String authCode = RandomUtil.randomString(16);
      entityDao.update("user", "email", email, "authCode", authCode);

      Entity entity = new Entity("email");
      entity.set("type", "4").set("email", email).set("toEmail", email);
      entity.set("message", authCode).set("status", 0).set("createTime", time);
      entityDao.save(entity);

      return ResponseBuilder.ok("email");

    } else if (StringUtils.isNotEmpty(phone)) {
      LOG.info("User " + phone + " submited a password remind request.");
      User user = entityDao.findOne("user", "phone", phone, UserRowMapper.getInstance());
      if (user == null) {
        return ResponseBuilder.error(10704, "您输入的手机号尚未注册。");
      }

      Long smsLastSendTime = (Long) getSessionAttribute("smsLastSendTime");
      if (smsLastSendTime != null && System.currentTimeMillis() - smsLastSendTime <= 60 * 1000) {
        return ResponseBuilder.error(201, "60秒内不可以重复发送验证码！");
      }

      String securityCode = RandomUtil.randomNumber(6);
      String[] datas = new String[] { securityCode };
      if (SmsUtilsYunpian.send(phone, "reset_password", datas)) {
        LOG.info("Send reset_password sms to phone: " + phone);
        setSessionAttribute("smsLastSendTime", time);

        entityDao.update("user", "phone", phone, "authCode", securityCode);

        Entity entity = new Entity("sms");
        entity.set("type", "reset_password").set("phone", phone);
        entity.set("securityCode", securityCode).set("sendTime", time);
        entity.set("status", 1).set("createTime", time);
        entityDao.save(entity);
      }

      return ResponseBuilder.ok("phone");
    }

    return ResponseBuilder.error(50000, "提交失败，请稍后重试。");
  }

  @GET
  @Path("reset")
  @Produces(MediaType.TEXT_HTML)
  public Response reset(@QueryParam("email") String email, @QueryParam("phone") String phone, @QueryParam("authCode") String authCode) {
    return Response.ok(new Viewable("reset")).build();
  }

}
