package com.transkip.web.api.v1;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
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
import com.yike.dao.mapper.InstructorRowMapper;
import com.yike.model.Course;
import com.yike.model.CourseApplication;
import com.yike.model.Instructor;
import com.yike.web.BaseResource;

/**
 * @author ilakeyc
 * @since 14/12/2016
 */

@Path("/api/v1/course")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiCourseResource extends BaseResource {
    private static final Log LOG = LogFactory.getLog(ApiCourseResource.class);

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> get(@PathParam("id") long courseId) {
        Course course = entityDao.get(Course.SQL_TABLE_NAME, courseId, CourseRowMapper.getInstance());
        if (course == null || course.isDisabled()) {
            return ResponseBuilder.error(60404, "未找到课程。");
        }

        setCourseProperties(course, getSessionUserId());

        return ResponseBuilder.ok(course);
    }


    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Map<String, Object> save(
            @FormParam("title") String title,
            @FormParam("instructorId") long instructorId,
            @FormParam("price") float price,
            @FormParam("teachingType") String teachingType,
            @FormParam("content") String content,
            @FormParam("description") String description,
            @FormParam("maximumLearnerCount") int maximumLearnerCount,
            @FormParam("free") int free,
            @FormParam("image") String image) {

        User user = getSessionUser();
        if (null == user) {
            return ResponseBuilder.ERR_NEED_LOGIN;
        }

        /*********** 检查表单 *************/
        Map<String, Object> checkResult = checkParameters(
                title,
                price,
                teachingType,
                content,
                description,
                maximumLearnerCount);
        if (null != checkResult) {
            return checkResult;
        }

        long userId = user.getId();
        if (0 == instructorId) {
            instructorId = getInstructorIdBy(userId);
        }
        if (0 == instructorId) {
            return ResponseBuilder.error(601013, "请先注册为讲师才能开课。");
        }

        /*********** 存储数据 *************/
        try {

            long createTime = System.currentTimeMillis();

            // Save course.
            Entity course = new Entity(Course.SQL_TABLE_NAME)

                    .set(Course.SQL_USER_ID, userId)
                    .set(Course.SQL_INSTRUCTOR_ID, instructorId)
                    .set(Course.SQL_TITLE, title)
                    .set(Course.SQL_PRICE, price)
                    .set(Course.SQL_TEACHING_TYPE, teachingType)
                    .set(Course.SQL_LEARNER_COUNT, maximumLearnerCount)
                    .set(Course.SQL_CONTENT, content)
                    .set(Course.SQL_DESCRIPTION, description)
                    .set(Course.SQL_CREATE_TIME, createTime)
                    .set(Course.SQL_FREE, free)
                    .set(Course.SQL_APPLIABLE, Course.APPLIABLE_TRUE)
                    .set(Course.SQL_STATUS, Constants.STATUS_NOT_READY);

            if (StringUtils.isNotEmpty(image)) {
                course.set(Course.SQL_IMAGE, image);
            }

            course = entityDao.saveAndReturn(course);

            return ResponseBuilder.ok(course.getId());

        } catch (Throwable t) {
            LOG.error("课程发布失败！", t);
            return ResponseBuilder.error(50000, "课程发布失败，请稍后再试。");
        }
    }

    /**
     * /api/v1/course/update
     * 更新已存在的课程数据，未改变属性也需要提交。
     *
     * @param courseId (req)
     */
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Map<String, Object> update(
            @FormParam("title") String title,
            @FormParam("price") float price,
            @FormParam("teachingType") String teachingType,
            @FormParam("content") String content,
            @FormParam("description") String description,
            @FormParam("maximumLearnerCount") int maximumLearnerCount,
            @FormParam("free") int free,
            @FormParam("id") long courseId,
            @FormParam("image") String image) {

        User user = getSessionUser();
        if (null == user) {
            return ResponseBuilder.ERR_NEED_LOGIN;
        }

        if (0 == courseId) {
            return ResponseBuilder.error(50001, "无效的课程ID");
        }

        Course course = entityDao.get(Course.SQL_TABLE_NAME, courseId, CourseRowMapper.getInstance());
        if (null == course) {
            return ResponseBuilder.error(60404, "该课程不存在。");
        }
        if (course.getAppliable() != Course.APPLIABLE_TRUE) {
            return ResponseBuilder.error(60202, "课程结束招生后不能编辑。");
        }

        long currentUserId = user.getId();
        long currentInstructorId = getInstructorIdBy(currentUserId);
        long courseUserId = course.getUserId();
        long courseInstructorId = course.getInstructorId();
        /************ 非课程发起人 或 非课程讲师 不能修改课程 ************/
        if (courseInstructorId != currentInstructorId && courseUserId != currentUserId) {
            return ResponseBuilder.error(60411, "你没有权限修改该课程。");
        }

        if (free == 1) {
            price = 0;
        }

        /*********** 检查表单 *************/
        Map<String, Object> checkResult = checkParameters(
                title,
                price,
                teachingType,
                content,
                description,
                maximumLearnerCount);
        if (null != checkResult) {
            return checkResult;
        }


        Map<String, Object> courseUpdateCondition = new HashMap<String, Object>();
        courseUpdateCondition.put("id", courseId);

        /*********** 更新数据 *************/
        Map<String, Object> updateValues = new HashMap<String, Object>();
        updateValues.put(Course.SQL_TITLE, title);

        updateValues.put(Course.SQL_TEACHING_TYPE, teachingType);
        updateValues.put(Course.SQL_CONTENT, content);
        updateValues.put(Course.SQL_DESCRIPTION, description);
        updateValues.put(Course.SQL_LEARNER_COUNT, maximumLearnerCount);
        updateValues.put(Course.SQL_FREE, free);
        updateValues.put(Course.SQL_PRICE, price);
        updateValues.put(Course.SQL_STATUS, Constants.STATUS_NOT_READY);

        if (StringUtils.isNotEmpty(image)) {
            updateValues.put(Course.SQL_IMAGE, image);
        }
        try {

            entityDao.update(Course.SQL_TABLE_NAME, courseUpdateCondition, updateValues);
            return ResponseBuilder.ok(courseId);

        } catch (Throwable t) {
            LOG.error("更新课程失败", t);
            return ResponseBuilder.error(50000, "无法更新课程信息，请稍后再试");
        }
    }


    /**
     * /api/v1/course/joined
     * 当前用户申请过的课程
     */
    // TODO `course_application表` 完成后继续
    @GET
    @Path("/joined")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> getJoinedCourse(
            @QueryParam("offset") @DefaultValue("0") long offset) {

        User user = getSessionUser();
        if (null == user) {
            return ResponseBuilder.ERR_NEED_LOGIN;
        }

        // 已经没有下一页数据。
        if (offset < 0) {
            return ResponseBuilder.ok(null, -1);
        }

        return null;
    }

    /**
     * /api/v1/course/$id/close
     * 停止一个课程的招生
     * 只有 课程发布人/讲师 有权限停止招生
     */
    @GET
    @Path("{id}/close")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> closeCourse(
            @PathParam("id") long courseId) {

        User user = getSessionUser();
        if (null == user) {
            return ResponseBuilder.ERR_NEED_LOGIN;
        }

        Course course = entityDao.get(Course.SQL_TABLE_NAME, courseId, CourseRowMapper.getInstance());
        if (null == course) {
            return ResponseBuilder.error(60404, "未找到课程。");
        }

        long currentUserId = user.getId();
        long currentInstructorId = getInstructorIdBy(currentUserId);
        long courseUserId = course.getUserId();
        long courseInstructorId = course.getInstructorId();
        /************ 非课程发起人 或 非课程讲师 不得停止课程招生 ************/
        if (courseInstructorId != currentInstructorId && courseUserId != currentUserId) {
            return ResponseBuilder.error(60411, "你没有权限结束该课程招生。");
        }

        try {
            Map<String, Object> courseUpdateCondition = new HashMap<String, Object>();
            courseUpdateCondition.put(Course.SQL_ID, courseId);
            Map<String, Object> courseUpdateValues = new HashMap<String, Object>();
            courseUpdateValues.put(Course.SQL_APPLIABLE, Course.APPLIABLE_FALSE);
            entityDao.update(Course.SQL_TABLE_NAME, courseUpdateCondition, courseUpdateValues);
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            LOG.error("课程结束招生失败", t);
            return ResponseBuilder.error(50000, "课程结束招生失败，请稍后再试");
        }
    }

    @GET
    @Path("{id}/remove")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> remove(
            @PathParam("id") long courseId) {

        User user = getSessionUser();
        if (null == user) {
            return ResponseBuilder.ERR_NEED_LOGIN;
        }

        Course course = entityDao.get(
                Course.SQL_TABLE_NAME,
                courseId,
                CourseRowMapper.getInstance());

        if (null == course) {
            return ResponseBuilder.error(60404, "未找到该课程");
        }
        if (course.getStatus() == Constants.STATUS_DELETED_BY_USER || course.getStatus() == Constants.STATUS_DELETED_BY_ADMIN
                || course.getStatus() == Constants.STATUS_DELETED_BY_REVIEW || course.getStatus() == Constants.STATUS_DELETED_BY_SYSTEM) {
            return ResponseBuilder.error(60001, "该课程已经被删除");
        }

        long currentUserId = user.getId();
        long courseUserId = course.getUserId();
        long courseInstructorId = course.getInstructorId();

        if (currentUserId != courseUserId && currentUserId != courseInstructorId && !user.isAdmin()) {
            return ResponseBuilder.error(60002, "你没有权限删除该课程");
        }

        int statusFlag = Constants.STATUS_DELETED_BY_USER;
        if (user.isAdmin()) {
            statusFlag = Constants.STATUS_DELETED_BY_ADMIN;
        }

        try {
            Map<String, Object> updateValues = new HashMap<String, Object>();
            updateValues.put(Course.SQL_STATUS, statusFlag);
            updateValues.put(Course.SQL_APPLIABLE, Course.APPLIABLE_FALSE);
            entityDao.update(Course.SQL_TABLE_NAME, Course.SQL_ID, courseId, updateValues);
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            LOG.error("unable to remove a course", t);
            return ResponseBuilder.error(50000, "暂时无法删除该课程");
        }
    }


    /**
     * 检查课程创建/更新的参数
     */
    private Map<String, Object> checkParameters(
            String title,
            float price,
            String teachingType,
            String content,
            String description,
            int maximumLearnerCount) {

        if (StringUtils.length(title) < 5) {
            return ResponseBuilder.error(60102, "标题不能少于5个字。");
        }
        if (StringUtils.length(title) > 30) {
            return ResponseBuilder.error(60103, "标题不能超过30个字。");
        }
        if (StringUtils.isEmpty(content)) {
            return ResponseBuilder.error(60104, "课程亮点不能为空。");
        }
        if (StringUtils.length(content) > 150) {
            return ResponseBuilder.error(60105, "课程亮点不能超过150字。");
        }
        if (StringUtils.isEmpty(description)) {
            return ResponseBuilder.error(60106, "详细介绍不能为空。");
        }
        if (StringUtils.length(description) > 3000) {
            return ResponseBuilder.error(60107, "详细介绍不能超过3000字。");
        }
        if (maximumLearnerCount < 0) {
            return ResponseBuilder.error(60108, "招生人数不能小于0。");
        }
        if (null == teachingType) {
            return ResponseBuilder.error(60109, "上课时间及方式不能为空。");
        }
        if (StringUtils.length(teachingType) > 2000) {
            return ResponseBuilder.error(60110, "上课时间及方式不能超过2000字。");
        }
        if (price > 99999) {
            return ResponseBuilder.error(601011, "价格不能多于5位数。");
        }
        if (maximumLearnerCount > 9999) {
            return ResponseBuilder.error(601012, "招生人数不能多于4位数。");
        }

        return null;
    }


    private void setCourseProperties(
            Course course,
            long userId) {

        // 添加课程是否报名状态
        long courseId = course.getId();
        if (0 != userId) {
            Map<String, Object> course_applicationSearchCondition = new HashMap<String, Object>();
            course_applicationSearchCondition.put(CourseApplication.SQL_USER_ID, userId);
            course_applicationSearchCondition.put(CourseApplication.SQL_COURSE_ID, courseId);
            boolean hasApplied = entityDao.exists(CourseApplication.SQL_TABLE_NAME, course_applicationSearchCondition);
            course.getProperties().put("joinStatus", (hasApplied ? 1 : 0));

        }

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

}
