package com.yike.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yike.dao.BaseDao;
import com.yike.util.StringUtil;
import com.sun.jersey.api.view.Viewable;
import com.yike.Constants;
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.dao.mapper.InstructorRowMapper;
import com.yike.model.Course;
import com.yike.model.Instructor;

/**
 * @author xiaolou
 * @since 2016年12月21日
 *
 */
@Path("/instructor")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class InstructorResource extends BaseResource {

  @GET
  @Path("{id}")
  @Produces(MediaType.TEXT_HTML)
  public Response get(@PathParam("id") long instructorId) {
    Instructor instructor = entityDao.get("instructor", instructorId, InstructorRowMapper.getInstance());
    if (instructor == null || instructor.isDisabled()) {
      request.setAttribute("_error", "找不到老師。");
      return Response.ok(new Viewable("instructor")).build();
    }

    request.setAttribute("instructor", instructor);

    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put(Course.SQL_INSTRUCTOR_ID, instructor.getId());
    condition.put("status", Constants.STATUS_ENABLED);
    List<Course> courses = entityDao.find(Course.SQL_TABLE_NAME, condition, 1, 20, CourseRowMapper.getInstance(), BaseDao.ORDER_OPTION_DESC);
    for (Course course : courses) {
      if (course == null || course.isDisabled()) {
        continue;
      }

      setCourseProperties(course);
      setSubscripts(course);
    }

    request.setAttribute("courses", courses);

    return Response.ok(new Viewable("instructor")).build();
  }

  private void setCourseProperties(Course course) {
    course.setDescription(StringUtil.replaceNewLine(course.getDescription()));
    course.setContent(StringUtil.replaceNewLine(course.getContent()));
  }

  private void setSubscripts(Course course) {
    if (course.getStatus() == Constants.STATUS_NOT_READY) {
      course.setSubscript("正在审核");
    } else if (course.getStatus() < Constants.STATUS_NOT_READY) {
      course.setSubscript("已删除");
    } else {
      if (course.getAppliable() == Course.APPLIABLE_FALSE) {
        StringBuilder sb = new StringBuilder();
        sb.append("已结束  ").append("共").append(course.getTotalLearnerCount()).append("人  ");
        if (StringUtils.isNotEmpty(course.getSubscript())) {
          sb.append(course.getSubscript());
        }
        course.setSubscript(sb.toString());
      } else {
        course.setSubscript("");
      }
    }

    String superScript = course.getSuperscript();
    course.setSuperscript("");
  }
}
