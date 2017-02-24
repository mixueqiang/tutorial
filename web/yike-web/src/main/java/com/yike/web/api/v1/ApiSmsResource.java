package com.yike.web.api.v1;

import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import com.yike.model.Entity;
import com.yike.util.RandomUtil;
import com.yike.util.ResponseBuilder;
import com.yike.util.SmsUtilsYunpian;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Map;

/**
 * @author mixueqiang
 * @since Apr 30, 2016
 */
@Path("/api/v1/sms")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiSmsResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiSmsResource.class);

  @Resource
  protected GenericManageableCaptchaService captchaService;

  @GET
  @Path("send")
  @Produces(APPLICATION_JSON)
  public Map<String, Object> send(@QueryParam("captchaCode") String captchaCode, @QueryParam("phone") String phone, @QueryParam("type") String type) {
    if (StringUtils.isEmpty(captchaCode)) {
      return ResponseBuilder.error(10600, "请输入图形验证码。");
    }

    String sessionId = WebUtils.getSessionId(request);
    try {
      if (!captchaService.validateResponseForID(sessionId, captchaCode)) {
        return ResponseBuilder.error(50000, "图形验证码不正确。");
      }
    } catch (Throwable t) {
      LOG.error("Failed to validate captcha code.", t);
      return ResponseBuilder.error(50000, "图形验证码不正确。");
    }

    if (StringUtils.isEmpty(phone)) {
      return ResponseBuilder.error(10601, "请输入手机号码。");
    }

    if (StringUtils.equals(type, "register") && entityDao.exists("user", "phone", phone)) {
      return ResponseBuilder.error(10602, "手机号码已经注册过。");

    } else if (StringUtils.equals(type, "reset_password") && !entityDao.exists("user", "phone", phone)) {
      return ResponseBuilder.error(10603, "手机号码尚未注册。");

    }

    long time = System.currentTimeMillis();
    // Avoid frequent sending requests.
    Long smsLastSentTime = (Long) getSessionAttribute("smsLastSentTime");
    if (smsLastSentTime != null && time - smsLastSentTime <= 60 * 1000) {
      return ResponseBuilder.error(10610, "60秒内不可以重复发送验证码！");
    }

    // Generate security code.
    String securityCode = RandomUtil.randomNumber(6);
    String[] datas = new String[] { securityCode };
    if (SmsUtilsYunpian.send(phone, type, datas)) {
      LOG.info("Send " + type + " sms to phone: " + phone);
      setSessionAttribute("smsLastSentTime", time);

      if (StringUtils.equals(type, "reset_password") || StringUtils.equals(type, "security")) {
        entityDao.update("user", "phone", phone, "securityCode", securityCode);
      }

      int businessType = StringUtils.equals(type, "register") ? 1 : 4;
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

}
