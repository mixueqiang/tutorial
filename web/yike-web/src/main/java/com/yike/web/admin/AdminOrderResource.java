package com.yike.web.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.view.Viewable;
import com.yike.dao.mapper.CourseApplicationRowMapper;
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.Course;
import com.yike.model.CourseApplication;
import com.yike.model.User;
import com.yike.util.PageNumberUtils;
import com.yike.util.Pair;
import com.yike.web.BaseResource;

/**
 * @author mixueqiang
 * @since Feb 18, 2017
 *
 */
@Path("/admin/order")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AdminOrderResource extends BaseResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index(@QueryParam("courseId") long courseId, @QueryParam("page") int page) {
    Map<String, Object> condition = new HashMap<String, Object>();
    if (courseId > 0) {
      condition.put("courseId", courseId);
    }
    condition.put("status", 1);
    Pair<Integer, List<CourseApplication>> result = entityDao.findAndCount("course_application", condition, 1, 20, CourseApplicationRowMapper.getInstance());

    List<CourseApplication> orders = result.right;
    for (CourseApplication order : orders) {
      User user = entityDao.get("user", order.getUserId(), UserRowMapper.getInstance());
      if (user != null) {
        order.getProperties().put("user", user);
      }
      Course course = entityDao.get("course", order.getCourseId(), CourseRowMapper.getInstance());
      if (course != null) {
        order.getProperties().put("course", course);
      }
    }
    request.setAttribute("orders", orders);

    // pagination.
    String uri = request.getRequestURI() + "?";
    String queryString = request.getQueryString();
    if (StringUtils.isNotEmpty(queryString)) {
      uri += queryString + "&p=";
    } else {
      uri += "p=";
    }

    uri = StringUtils.replace(uri, "p=" + page + "&", "");
    request.setAttribute("uriPrefix", uri);

    Pair<List<Integer>, Integer> pages = PageNumberUtils.generate(page, result.left);
    request.setAttribute("currentPage", page);
    request.setAttribute("pages", pages.left);
    request.setAttribute("lastPage", pages.right);

    return Response.ok(new Viewable("index")).build();
  }

}
