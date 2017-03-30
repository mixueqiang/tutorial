package com.yike.web.dashboard;

import com.yike.model.User;
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
 * @since Jun 2, 2014
 */
@Path("/dashboard")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DashboardIndexResource extends BaseResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    User user = getSessionUser();
    if (user == null) {
      return signinAndGoto(request.getRequestURI());
    }

    return redirect("/courses");
  }

}
