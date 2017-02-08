package com.yike.web;

import com.sun.jersey.api.view.Viewable;
import com.yike.Constants;
import com.yike.dao.BaseDao;
import com.yike.dao.mapper.*;
import com.yike.model.*;
import com.yike.util.PageNumberUtils;
import com.yike.util.Pair;
import com.yike.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xueqiangmi
 * @since May 28, 2015
 */
@Path("/course")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CourseResource extends BaseResource {

  @GET
  @Path("create")
  @Produces(MediaType.TEXT_HTML)
  public Response create() {
    List<Category> categories = entityDao.find(Category.SQL_TABLE_NAME, Category.SQL_STATUS, Constants.STATUS_OK, CategoryRowMapper.getInstance());
    request.setAttribute("categories", categories);
    return Response.ok(new Viewable("create")).build();
  }

  @GET
  @Path("{id}/edit")
  @Produces(MediaType.TEXT_HTML)
  public Response edit(@PathParam("id") long courseId) {

    Course course = entityDao.get(Course.SQL_TABLE_NAME, courseId, CourseRowMapper.getInstance());
    if (course == null) {
      request.setAttribute("_blank", true);
      request.setAttribute("_msg", "课程未找到。");
      return Response.ok(new Viewable("course")).build();
    }
    User user = getSessionUser();
    if (null == user) {
      return signinAndGoback();
    }
    long currentInstructorId = getInstructorIdBy(user.getId());
    long courseInstructorId = course.getInstructorId();
    if (currentInstructorId != courseInstructorId) {
      request.setAttribute("_blank", true);
      request.setAttribute("_msg", "你没有权限编辑该课程");
      return Response.ok(new Viewable("course")).build();
    }

    request.setAttribute("course", course);

    return Response.ok(new Viewable("edit")).build();
  }


  @GET
  @Path("{id}")
  @Produces(MediaType.TEXT_HTML)
  public Response get(@PathParam("id") long id) {

    Course course = entityDao.get(Course.SQL_TABLE_NAME, id, CourseRowMapper.getInstance());

    long userId = getSessionUserId();
    boolean courseExist = (course != null) && (course.getStatus() >= Constants.STATUS_NOT_READY);
    boolean courseNotReady = (course != null) && course.getStatus() == Constants.STATUS_READY;
    boolean isCourseInstructor = (course != null) && course.getInstructorId() == getInstructorIdBy(userId);

    if (!courseExist || (courseNotReady && !isCourseInstructor)) {
      request.setAttribute("_blank", true);
      request.setAttribute("_msg", "课程未找到。");
      return Response.ok(new Viewable("course")).build();
    }

    setCourseProperties(course);
    request.setAttribute("course", course);
    request.setAttribute("instructor", course.getProperties().get("instructor"));
    request.setAttribute("hasApplied", checkApplication(course.getId()));

    return Response.ok(new Viewable("course")).build();
  }

  @GET
  @Path("{id}/students")
  @Produces(MediaType.TEXT_HTML)
  public Response students(@PathParam("id") long courseId) {
    Course course = entityDao.get("course", courseId, CourseRowMapper.getInstance());
    if (course == null) {
      request.setAttribute("_blank", true);
      request.setAttribute("_msg", "课程未找到。");
      return Response.ok(new Viewable("course")).build();
    }
    // 检查登录
    User user = getSessionUser();
    if (null == user) {
      return signinAndGoback();
    }
    long userId = user.getId();
    long instructorId = getInstructorIdBy(userId);
    // 检查是否为讲师
    if (0 == instructorId) {
      request.setAttribute("_error", "你的角色不是老师，没有权限查看名单。");
      request.setAttribute("_blank", true);
      return Response.ok(new Viewable("course")).build();
    }
    // 检查课程讲师
    if (instructorId != course.getInstructorId() && userId != course.getUserId()) {
      request.setAttribute("_error", "你不是该课程的老师，没有权限查看名单。");
      request.setAttribute("_blank", true);
      return Response.ok(new Viewable("course")).build();
    }

    // 取出报名名单
    Map<String, Object> courseApplicationFindCondition = new HashMap<String, Object>();
    courseApplicationFindCondition.put(CourseApplication.SQL_COURSE_ID, courseId);
    courseApplicationFindCondition.put(CourseApplication.SQL_PROGRESS, 2);
    List<Entity> applicationEntities = entityDao.find(CourseApplication.SQL_TABLE_NAME, courseApplicationFindCondition, 0, 200, CourseApplication.SQL_CREATE_TIME, BaseDao.ORDER_OPTION_DESC);

    // 组装学生模型
    List<Student> students = new ArrayList<Student>();
    for (Entity entity : applicationEntities) {
      long applicationId = entity.getId();
      long studentId = entity.getLong(CourseApplication.SQL_USER_ID);
      User applyUser = entityDao.get("user", studentId, UserRowMapper.getInstance());
      if (null == applyUser) {
        continue;
      }

      Student student = new Student();
      student.setName(entity.getString(CourseApplication.SQL_NAME));
      student.setId(applyUser.getId());
      student.setNickname(applyUser.getName());
      student.setApplyId(applicationId);
      student.setCourseId(courseId);
      student.setPhone(entity.getString(CourseApplication.SQL_PHONE));
      student.setCreateTime(entity.getLong(CourseApplication.SQL_CREATE_TIME));
      students.add(student);
    }
    request.setAttribute("students", students);
    request.setAttribute("course", course);
    return Response.ok(new Viewable("studentsList")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index(
          @QueryParam("c") @DefaultValue("0") long categoryId,
          @QueryParam("p") @DefaultValue("1") int page) {

    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put("status", Constants.STATUS_ENABLED);

    if (categoryId != 0) {
      if (entityDao.exists(Category.SQL_TABLE_NAME, Category.SQL_ID, categoryId)) {
        request.setAttribute("currentCategoryId", categoryId);
        condition.put(Course.SQL_CATEGORY_ID, categoryId);
      }
    }

    Pair<Integer, List<Course>> result = entityDao.findAndCount("course", condition, page, PageNumberUtils.PAGE_SIZE_SMALL, CourseRowMapper.getInstance());
    List<Course> courses = result.right;
    request.setAttribute("courses", courses);

    List<Category> categories = entityDao.find(
            Category.SQL_TABLE_NAME,
            Category.SQL_STATUS,
            Constants.STATUS_OK,
            CategoryRowMapper.getInstance(),
            Category.SQL_RANK,
            BaseDao.ORDER_OPTION_ASC);

    for (Course course : courses) {
      if (course == null || course.isDisabled()) {
        continue;
      }
      setCourseProperties(course);
      String categoryForSubscript = null;
      if (categoryId == 0) {
        categoryForSubscript = ((Category)course.getProperties().get("category")).getName();
      }
      setSubscripts(course, categoryForSubscript);
    }

    // pagination.
    long count = result.left;
    Pair<List<Integer>, Integer> pages = PageNumberUtils.generate(page, count, 10);
    request.setAttribute("categories", categories);
    request.setAttribute("currentPage", page);
    request.setAttribute("pages", pages.left);
    request.setAttribute("lastPage", pages.right);
    return Response.ok(new Viewable("index")).build();
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
    Category category = getCategory(course.getCategoryId());
    if (null != category && category.getStatus() == Constants.STATUS_OK) {
      course.getProperties().put("category", category);
    }
  }

  private boolean checkApplication(long courseId) {
    User user = getSessionUser();
    if (null == user) {
      return false;
    }
    long userId = user.getId();

    Map<String, Object> condition = new HashMap<String, Object>();

    condition.put(CourseApplication.SQL_USER_ID, userId);
    condition.put(CourseApplication.SQL_COURSE_ID, courseId);
    condition.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);

    CourseApplication courseApplication = entityDao.findOne(
            CourseApplication.SQL_TABLE_NAME,
            condition,
            CourseApplicationRowMapper.getInstance());

    boolean hasApply = courseApplication != null;

    return hasApply;
  }

  private long getInstructorIdBy(
          long userId) {

    Instructor instructor = entityDao.findOne(
            Instructor.SQL_TABLE_NAME,
            Instructor.SQL_USER_ID,
            userId,
            InstructorRowMapper.getInstance());

    if (null == instructor) {
      return 0;
    }

    return instructor.getId();
  }

  private void setSubscripts(Course course, String category) {
    StringBuilder sb = new StringBuilder();
    if (StringUtils.isNotEmpty(category)) {
      sb.append(category).append("   ");
    }
    if (course.getStatus() == Constants.STATUS_NOT_READY) {
      sb.append("正在审核");
    } else if (course.getStatus() < Constants.STATUS_NOT_READY) {
      sb.append("已删除");
    } else {
      if (course.getAppliable() == Course.APPLIABLE_FALSE) {
        sb.append("已结束   ");
      }
      if (course.isCountShow()) {
        sb.append("共").append(course.getCount()).append("人   ");
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
