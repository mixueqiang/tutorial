package com.yike.web;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yike.Constants;
import com.yike.model.User;
import com.sun.jersey.api.view.Viewable;
import com.yike.model.Bid;
import com.yike.model.Project;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author mixueqiang
 * @since Mar 10, 2014
 */
@Path("/dashboard")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DashboardResource extends BaseResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    User user = getSessionUser();
    if (user == null) {
      return signinAndGoto(request.getRequestURI());
    }

    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("buyer")
  @Produces(MediaType.TEXT_HTML)
  public Response buyer() {
    User user = getSessionUser();
    if (user == null) {
      return signinAndGoto(request.getRequestURI());
    }

    List<Project> projects = projectDao.getByUser(user.getId(), 1, 100);
    List<Project> openProjects = new ArrayList<Project>();
    List<Project> workProjects = new ArrayList<Project>();
    List<Project> pastProjects = new ArrayList<Project>();

    for (Project project : projects) {
      int status = project.getStatus();
      if (status == Constants.PROJECT_STATUS_OPEN) {
        openProjects.add(project);
      } else if (status == Constants.PROJECT_STATUS_START || status == Constants.PROJECT_STATUS_MILESTONE
          || status == Constants.PROJECT_STATUS_PROBLEM) {
        Bid bid = bidDao.getChosenBid(project.getId());
        bid.setUser(userDao.get(bid.getUserId()));
        project.setBid(bid);
        workProjects.add(project);
      } else {
        pastProjects.add(project);
      }
    }
    request.setAttribute("openProjects", openProjects);
    request.setAttribute("workProjects", workProjects);
    request.setAttribute("pastProjects", pastProjects);

    return Response.ok(new Viewable("buyer")).build();
  }

  @GET
  @Path("seller")
  @Produces(MediaType.TEXT_HTML)
  public Response seller() {
    User user = getSessionUser();
    if (user == null) {
      return signinAndGoto(request.getRequestURI());
    }

    List<Bid> bids = bidDao.getBidded(user.getId(), 1, 100);
    List<Project> projects = new ArrayList<Project>();
    for (Bid bid : bids) {
      projects.add(projectDao.get(bid.getProjectId()));
    }
    request.setAttribute("projects", projects);

    return Response.ok(new Viewable("seller")).build();
  }

  @GET
  @Path("project")
  public Response project(@QueryParam("project_id") long projectId) {
    Project project = projectDao.get(projectId);
    request.setAttribute("project", project);

    return Response.ok(new Viewable("project")).build();
  }

  @GET
  @Path("account")
  @Produces(MediaType.TEXT_HTML)
  public Response account() {
    User user = getSessionUser();
    if (user == null) {
      return signinAndGoto(request.getRequestURI());
    }

    return Response.ok(new Viewable("account")).build();
  }

}
