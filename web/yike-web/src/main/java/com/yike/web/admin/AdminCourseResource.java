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
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.dao.mapper.InstructorRowMapper;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.Course;
import com.yike.model.Instructor;
import com.yike.model.User;
import com.yike.util.PageNumberUtils;
import com.yike.util.Pair;
import com.yike.web.BaseResource;

/**
 * @author mixueqiang
 * @since Feb 18, 2017
 *
 */
@Path("/admin/course")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AdminCourseResource extends BaseResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index(@QueryParam("categoryId") long categoryId, @QueryParam("page") int page) {
    Map<String, Object> condition = new HashMap<String, Object>();
    if (categoryId > 0) {
      condition.put("categoryId", categoryId);
    }
    condition.put("status", 1);
    Pair<Integer, List<Course>> result = entityDao.findAndCount("course", condition, 1, 20, CourseRowMapper.getInstance());

    List<Course> courses = result.right;
    for (Course course : courses) {
      User user = entityDao.get("user", course.getUserId(), UserRowMapper.getInstance());
      if (user != null) {
        course.getProperties().put("user", user);
      }
      Instructor instructor = entityDao.get("instructor", course.getInstructorId(), InstructorRowMapper.getInstance());
      if (course != null) {
        course.getProperties().put("instructor", instructor);
      }
    }
    request.setAttribute("courses", courses);

    // pagination.
    String uri = request.getRequestURI() + "?";
    String queryString = request.getQueryString();
    if (StringUtils.isNotEmpty(queryString)) {
      uri += queryString + "&page=";
    } else {
      uri += "page=";
    }

    uri = StringUtils.replace(uri, "page=" + page + "&", "");
    request.setAttribute("uriPrefix", uri);

    Pair<List<Integer>, Integer> pages = PageNumberUtils.generate(page, result.left);
    request.setAttribute("currentPage", page);
    request.setAttribute("pages", pages.left);
    request.setAttribute("lastPage", pages.right);

    return Response.ok(new Viewable("index")).build();
  }

}
