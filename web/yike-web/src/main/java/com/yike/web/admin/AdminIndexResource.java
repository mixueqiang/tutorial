package com.yike.web.admin;

import com.sun.jersey.api.view.Viewable;
import com.yike.web.BaseResource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mixueqiang
 * @since Mar 7, 2014
 */
@Path("/admin")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AdminIndexResource extends BaseResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    long userCount = entityDao.count("user");
      long itUserCount = entityDao.count("wx_user", "subscribe", 1);
      long yikeUserCount = entityDao.count("wx_yike_user", "subscribe", 1);
    request.setAttribute("userCount", userCount);
    request.setAttribute("itUserCount", itUserCount);
    request.setAttribute("yikeUserCount", yikeUserCount);
    return Response.ok(new Viewable("index")).build();
  }

}
