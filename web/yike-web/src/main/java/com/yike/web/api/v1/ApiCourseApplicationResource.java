package com.yike.web.api.v1;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.yike.dao.mapper.CourseApplicationRowMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yike.model.Entity;
import com.yike.model.User;
import com.yike.util.ResponseBuilder;
import com.yike.Constants;
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.model.Course;
import com.yike.model.CourseApplication;
import com.yike.web.BaseResource;

/**
 * @author ilakeyc
 * @since 14/12/2016
 */

@Path("/api/v1/course/application")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiCourseApplicationResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiCourseApplicationResource.class);

  @GET
  @Path("check")
  @Produces(APPLICATION_JSON)
  public Map<String, Object> check(@QueryParam("courseId") long courseId) {
    return checkAppliable(courseId);
  }

  /**
   * 付款完成后，提交订单号码
   */
  @POST
  @Path("paid")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> applyCompleted(@FormParam("id") long id, @FormParam("courseId") long courseId, @FormParam("oid") String orderId) {

    /*********** 检查是否可以申请 *************/
    Map<String, Object> checkResult = checkAppliable(courseId);
    if ((Integer) checkResult.get("e") > 0) {
      return checkResult;
    }

    /*********** 提交申请 *************/
    try {
      long time = System.currentTimeMillis();

      // 更新订单
      Map<String, Object> courseApplicationUpdateCondition = new HashMap<String, Object>();
      courseApplicationUpdateCondition.put(CourseApplication.SQL_ID, id);
      courseApplicationUpdateCondition.put(CourseApplication.SQL_COURSE_ID, courseId);

      Map<String, Object> courseApplicationUpdateValues = new HashMap<String, Object>();
      courseApplicationUpdateValues.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);
      courseApplicationUpdateValues.put(CourseApplication.SQL_ORDER_ID, orderId);
      courseApplicationUpdateValues.put(CourseApplication.SQL_UPDATE_TIME, time);

      entityDao.update(CourseApplication.SQL_TABLE_NAME, courseApplicationUpdateCondition, courseApplicationUpdateValues);
      Course course = entityDao.get(Course.SQL_TABLE_NAME, courseId, CourseRowMapper.getInstance());
      float price = course.getPrice();
      // 存入报名人
      Entity entity = new Entity(CourseApplication.SQL_TABLE_NAME).set(CourseApplication.SQL_USER_ID, getSessionUserId()).set(CourseApplication.SQL_COURSE_ID, courseId)
          .set(CourseApplication.SQL_PRICE, price).set(CourseApplication.SQL_ORDER_ID, orderId).set(CourseApplication.SQL_CREATE_TIME, time);
      entityDao.save(entity);

      // 更新申请人数
      Map<String, Object> courseCountCondition = new HashMap<String, Object>();
      courseCountCondition.put(CourseApplication.SQL_COURSE_ID, courseId);
      courseCountCondition.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);
      int count = entityDao.count(CourseApplication.SQL_TABLE_NAME, courseCountCondition);
      Map<String, Object> courseUpdateCondition = new HashMap<String, Object>();
      courseUpdateCondition.put(Course.SQL_ID, courseId);
      entityDao.update(Course.SQL_TABLE_NAME, courseUpdateCondition, Course.SQL_CURRENT_LEARNER_COUNT, count);

      return ResponseBuilder.OK;

    } catch (Throwable t) {
      LOG.error("Failed to apply a course.", t);
      return ResponseBuilder.error(102, t.getMessage());
    }
  }

  /**
   * 提交订单，准备付款
   *
   * @return 返回报名记录的id
   */
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> save(@FormParam("courseId") long courseId, @FormParam("name") String name, @FormParam("phone") String phone) {
    /*********** 检查是否可以申请 *************/
    Map<String, Object> checkResult = checkAppliable(courseId);
    if ((Integer) checkResult.get("e") > 0) {
      return checkResult;
    }

    /*********** 提交申请 *************/
    long time = System.currentTimeMillis();
    try {
      Course course = entityDao.get(Course.SQL_TABLE_NAME, courseId, CourseRowMapper.getInstance());

      // 存入报名人
      Entity entity = new Entity(CourseApplication.SQL_TABLE_NAME);
      entity.set(CourseApplication.SQL_USER_ID, getSessionUserId()).set(CourseApplication.SQL_COURSE_ID, courseId);
      entity.set(CourseApplication.SQL_NAME, name).set(CourseApplication.SQL_PHONE, phone);
      if (course.getFree() == 1) {
        entity.set(CourseApplication.SQL_PRICE, 0.00f);
        entity.set(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);

      } else {
        entity.set("price", course.getPrice());
        entity.set(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_NON_ORDER);
      }
      entity.set(CourseApplication.SQL_STATUS, Constants.STATUS_ENABLED).set(CourseApplication.SQL_CREATE_TIME, time);
      entity = entityDao.saveAndReturn(entity);

      // 更新申请人数
      Map<String, Object> courseCountCondition = new HashMap<String, Object>();
      courseCountCondition.put(CourseApplication.SQL_COURSE_ID, courseId);
      courseCountCondition.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);
      int count = entityDao.count(CourseApplication.SQL_TABLE_NAME, courseCountCondition);
      Map<String, Object> courseUpdateCondition = new HashMap<String, Object>();
      courseUpdateCondition.put(Course.SQL_ID, courseId);
      entityDao.update(Course.SQL_TABLE_NAME, courseUpdateCondition, Course.SQL_CURRENT_LEARNER_COUNT, count);

      return ResponseBuilder.ok(entity);

    } catch (Throwable t) {
      LOG.error("Failed to apply a course.", t);
      return ResponseBuilder.error(50000, "报名失败。");
    }
  }

  @GET
  @Path("{id}/cancel")
  @Produces(APPLICATION_JSON)
  public Map<String, Object> applicationCancel(@PathParam("id") long courseId) {

    User user = getSessionUser();
    if (null == user) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    Map<String, Object> courseApplicationFindCondition = new HashMap<String, Object>();
    courseApplicationFindCondition.put(CourseApplication.SQL_COURSE_ID, courseId);
    courseApplicationFindCondition.put(CourseApplication.SQL_USER_ID, user.getId());

    CourseApplication application = entityDao.findOne(CourseApplication.SQL_TABLE_NAME, courseApplicationFindCondition, CourseApplicationRowMapper.getInstance());

    if (null == application) {
      return ResponseBuilder.error(60404, "你还没有报名该课程。");
    }

    if (application.getProgress() == CourseApplication.PROGRESS_CANCELED) {
      return ResponseBuilder.error(60100, "你已经取消了该课程。");
    }

    Course course = entityDao.get(Course.SQL_TABLE_NAME, courseId, CourseRowMapper.getInstance());
    if (null == course) {
      return ResponseBuilder.error(60404, "课程不存在");
    }

    if (application.getProgress() == CourseApplication.PROGRESS_PAID) {
      if (application.getPrice() != 0) {
        return ResponseBuilder.error(60101, "你已经付款了该课程，请加QQ群：475581666，或者发送邮件到：service@transkip.com 提供你的帐号信息、需要取消的课程链接以及你的支付宝收款帐号。");
      }
    }

    try {

      Map<String, Object> courseApplicationUpdateValues = new HashMap<String, Object>();
      courseApplicationUpdateValues.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_CANCELED);

      entityDao.update(CourseApplication.SQL_TABLE_NAME, courseApplicationFindCondition, courseApplicationUpdateValues);

      // 更新申请人数
      Map<String, Object> courseCountCondition = new HashMap<String, Object>();
      courseCountCondition.put(CourseApplication.SQL_COURSE_ID, courseId);
      courseCountCondition.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);
      int count = entityDao.count(CourseApplication.SQL_TABLE_NAME, courseCountCondition);
      Map<String, Object> courseUpdateCondition = new HashMap<String, Object>();
      courseUpdateCondition.put(Course.SQL_ID, courseId);
      entityDao.update(Course.SQL_TABLE_NAME, courseUpdateCondition, Course.SQL_CURRENT_LEARNER_COUNT, count);

      return ResponseBuilder.OK;

    } catch (Throwable t) {
      LOG.error("failure to cancel course application", t);
      return ResponseBuilder.error(50000, "暂时无法取消该课程，请稍后再试。");
    }
  }

  private Map<String, Object> checkAppliable(long courseId) {
    User user = getSessionUser();
    if (null == user) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    /*********** 检查课程是否存在 *************/
    Course course = entityDao.get(Course.SQL_TABLE_NAME, courseId, CourseRowMapper.getInstance());
    if (null == course) {
      return ResponseBuilder.error(60404, "未找到课程：课程可能已经暂停或停止招生。");
    }

    /*********** 检查课程是否可以申请 *************/
    if (course.getAppliable() == Course.APPLIABLE_FALSE) {
      return ResponseBuilder.error(60110, "课程已经停止招生。");
    }
    if (course.getStatus() != Constants.STATUS_OK) {
      return ResponseBuilder.error(60112, "该课程还没有通过审核，暂时不能报名。");
    }

    /*********** 检查课程是否已经申请 *************/
    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put(CourseApplication.SQL_USER_ID, user.getId());
    condition.put(CourseApplication.SQL_COURSE_ID, courseId);
    condition.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);
    if (entityDao.exists(CourseApplication.SQL_TABLE_NAME, condition)) {
      return ResponseBuilder.error(60111, "你已经报名过该课程。");
    }

    return ResponseBuilder.OK;
  }

}
