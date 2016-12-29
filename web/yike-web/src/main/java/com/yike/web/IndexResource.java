package com.yike.web;

import java.net.URI;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import com.yike.service.SessionService;

/**
 * @author mixueqiang
 * @since Oct 12, 2014
 */
@Path("/")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class IndexResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(IndexResource.class);

  @Resource
  protected SessionService sessionService;

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("signin")
  @Produces(MediaType.TEXT_HTML)
  public Response signin() {
    return Response.ok(new Viewable("signin")).build();
  }

  @GET
  @Path("signout")
  @Produces(MediaType.TEXT_HTML)
  public Response signout() {
    try {
      // Clear sessionId.
      String sessionId = WebUtils.getSessionId(request);
      if (StringUtils.isNotEmpty(sessionId)) {
        sessionService.destorySession(sessionId);
      }

      request.getSession().invalidate();
      return Response.seeOther(new URI("/")).build();

    } catch (Throwable t) {
      LOG.error("User can not sign out.", t);
      return null;
    }
  }

  @GET
  @Path("signup")
  @Produces(MediaType.TEXT_HTML)
  public Response signup() {
    return Response.ok(new Viewable("signup")).build();
  }

}
