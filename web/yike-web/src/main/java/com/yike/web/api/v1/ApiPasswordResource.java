package com.yike.web.api.v1;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yike.model.Entity;
import com.yike.util.ResponseBuilder;
import com.yike.web.BaseResource;

/**
 * @author mixueqiang
 * @since Sep 24, 2016
 *
 */
@Path("/api/v1/password")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiPasswordResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiPasswordResource.class);

  @POST
  @Path("reset")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> reset(@FormParam("id") String id, @FormParam("securityCode") String securityCode, @FormParam("password") String password) {
    LOG.info("User " + id + " requests to reset the password.");

    if (StringUtils.isEmpty(id) || StringUtils.isEmpty(securityCode) || StringUtils.isEmpty(password)) {
      return ResponseBuilder.error(10705, "缺少参数。");
    }

    Entity user = null;
    if (StringUtils.contains(id, "@")) {
      user = entityDao.findOne("user", "email", id);
    } else {
      user = entityDao.findOne("user", "phone", id);
    }

    if (user == null || !StringUtils.equals(securityCode, user.getString("authCode"))) {
      return ResponseBuilder.error(10711, "无效的验证码，请重新找回密码。");
    }

    try {
      // Reset user password.
      Map<String, Object> updateValues = new HashMap<String, Object>();
      updateValues.put("password", password);
      updateValues.put("authCode", "");
      entityDao.update("user", "id", user.getId(), updateValues);

      LOG.info("User " + id + " reset the password.");
      return ResponseBuilder.OK;

    } catch (Throwable t) {
      LOG.error("Failed to reset user password: " + id, t);
      return ResponseBuilder.error(50000, "设置密码失败，请稍后再试!");
    }
  }

}
