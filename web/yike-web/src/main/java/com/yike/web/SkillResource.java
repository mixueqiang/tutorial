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
 * 
 * @author mixueqiang
 * @since Jan 17, 2017
 *
 */
@Path("/skill")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SkillResource extends BaseResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {

    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("{name}")
  @Produces(MediaType.TEXT_HTML)
  public Response get(@PathParam("name") String name) {

    return Response.ok(new Viewable("skill")).build();
  }

}
