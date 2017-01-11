package com.yike.web.admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.view.Viewable;
import com.yike.model.User;
import com.yike.web.BaseResource;

/**
 * @author mixueqiang
 * @since Dec 27, 2016
 *
 */
@Path("/admin/image")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AdminImageResource extends BaseResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    User user = getSessionUser();
    if (user == null) {
      return signinAndGoback();
    }

    return Response.ok(new Viewable("index")).build();
  }

}
