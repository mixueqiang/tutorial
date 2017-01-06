package com.yike.web;

import com.sun.jersey.api.view.Viewable;
import com.yike.Constants;
import com.yike.dao.mapper.CategoryRowMapper;
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.dao.mapper.InstructorRowMapper;
import com.yike.model.Category;
import com.yike.model.Course;
import com.yike.model.Instructor;
import com.yike.model.User;
import com.yike.service.SessionService;
import com.yike.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<Category> allCategories = entityDao.find(Category.SQL_TABLE_NAME, Category.SQL_STATUS, Constants.STATUS_OK, CategoryRowMapper.getInstance());

    Map<Long, List<Course>> courseMap = new HashMap<Long, List<Course>>();
    List<Category> categories = new ArrayList<Category>();

    for (Category category : allCategories) {

      long categoryId = category.getId();

      Map<String, Object> courseFindCondition = new HashMap<String, Object>();
      courseFindCondition.put(Course.SQL_STATUS, Constants.STATUS_READY);
      courseFindCondition.put(Course.SQL_APPLIABLE, Course.APPLIABLE_TRUE);
      courseFindCondition.put(Course.SQL_CATEGORY_ID, categoryId);

      List<Course> courses = entityDao.find(
              Course.SQL_TABLE_NAME,
              courseFindCondition,
              0,
              4,
              CourseRowMapper.getInstance());

      if (!courses.isEmpty()) {

        categories.add(category);

        for (Course course : courses) {
          setCourseProperties(course);
          setSubscripts(course);
        }

        courseMap.put(categoryId, courses);
      }

    }
    User user = getSessionUser();
    if (null != user) {
      request.setAttribute("username", user.getName());
    }
    request.setAttribute("isLogin", null != user);
    request.setAttribute("categories", categories);
    request.setAttribute("courses", courseMap);

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
  public Response signup() throws Throwable {
    return signupWithMobile();
  }


  @GET
  @Path("signup/email")
  @Produces(MediaType.TEXT_HTML)
  public Response signupWithEmail() throws Throwable {
    return Response.ok(new Viewable("signup_email")).build();
  }

  @GET
  @Path("signup/mobile")
  @Produces(MediaType.TEXT_HTML)
  public Response signupWithMobile() throws Throwable {
    return Response.ok(new Viewable("signup_mobile")).build();
  }

  private void setCourseProperties(Course course) {
    course.setTeachingType(StringUtil.replaceNewLine(course.getTeachingType()));
    course.setDescription(StringUtil.replaceNewLine(course.getDescription()));
    course.setContent(StringUtil.replaceNewLine(course.getContent()));

    if (course.getInstructorId() > 0) {
      Instructor instructor = entityDao.get(Instructor.SQL_TABLE_NAME, course.getInstructorId(), InstructorRowMapper.getInstance());
      if (null != instructor) {
        course.getProperties().put("instructor", instructor);
      }
    }
  }

  private void setSubscripts(Course course) {
    StringBuilder sb = new StringBuilder();
    if (course.getStatus() == Constants.STATUS_NOT_READY) {
      sb.append("正在审核");
    } else if (course.getStatus() < Constants.STATUS_NOT_READY) {
      sb.append("已删除");
    } else {
      if (course.getAppliable() == Course.APPLIABLE_FALSE) {
        sb.append("已结束  ");
      }
      if (course.isCountShow()) {
        sb.append("共").append(course.getCount()).append("人  ");
      }
      if (StringUtils.isNotEmpty(course.getSubscript())) {
        sb.append(course.getSubscript());
      }
    }
    course.setSubscript(sb.toString());
    String superScript = course.getSuperscript();
    course.setSuperscript("");
  }

}
