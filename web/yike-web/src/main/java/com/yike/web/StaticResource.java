package com.yike.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.view.Viewable;

/**
 * @author mixueqiang
 * @since Dec 30, 2016
 *
 */
@Path("/static")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class StaticResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("{page}")
  @Produces(MediaType.TEXT_HTML)
  public Response page(@PathParam("page") String page) {
    return Response.ok(new Viewable(page)).build();
  }

}
