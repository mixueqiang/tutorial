package com.yike.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yike.util.PageNumberUtils;
import com.yike.util.Pair;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yike.dao.BaseDao;
import com.yike.model.User;
import com.yike.util.StringUtil;
import com.sun.jersey.api.view.Viewable;
import com.yike.Constants;
import com.yike.dao.mapper.CourseApplicationRowMapper;
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.dao.mapper.InstructorRowMapper;
import com.yike.model.Course;
import com.yike.model.CourseApplication;
import com.yike.model.Instructor;

/**
 * @author mixueqiang
 * @since Dec 16, 2016
 */
@Path("/courses")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CoursesResource extends BaseResource {

    @GET
    @Path("as_an_instructor")
    @Produces(MediaType.TEXT_HTML)
    public Response getByInstructor() {
        User user = getSessionUser();
        if (user == null) {
            return signinAndGoback();
        }

        Instructor instructor = entityDao.findOne("instructor", "userId", user.getId(), InstructorRowMapper.getInstance());
        if (instructor == null) {
            request.setAttribute("_error", "你的角色不是老师，没有发布课程。");
            request.setAttribute("_blank", true);
            return Response.ok(new Viewable("instructor")).build();
        }

        request.setAttribute("instructor", instructor);


        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(Course.SQL_INSTRUCTOR_ID, instructor.getId());
        Map<Pair<String, String>, Object> offsets = new HashMap<Pair<String, String>, Object>();
        offsets.put(new Pair<String, String>("status", BaseDao.ORDER_OPTION_ASC), -1);
        offsets.put(new Pair<String, String>("status", BaseDao.ORDER_OPTION_DESC), 2);
        List<Course> courses = entityDao.findByOffset(
                Course.SQL_TABLE_NAME,
                condition, offsets,
                Course.SQL_ID,
                BaseDao.ORDER_OPTION_DESC,
                PageNumberUtils.PAGE_SIZE_MEDIUM,
                CourseRowMapper.getInstance());

        for (Course course : courses) {
            if (0 == course.getCurrentLearnerCount()) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("共").append(course.getCurrentLearnerCount()).append("人");
            course.setSubscript(sb.toString());
        }

        request.setAttribute("courses", courses);

        return Response.ok(new Viewable("instructor")).build();
    }

    @GET
    @Path("as_a_student")
    @Produces(MediaType.TEXT_HTML)
    public Response getByUser() {
        User user = getSessionUser();
        if (user == null) {
            return signinAndGoback();
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("userId", user.getId());
        condition.put("status", Constants.STATUS_ENABLED);
        List<CourseApplication> courseApplications = entityDao.find("course_application", condition, 1, 20, CourseApplicationRowMapper.getInstance(), BaseDao.ORDER_OPTION_DESC);
        List<Course> courses = new ArrayList<Course>();
        for (CourseApplication courseApplication : courseApplications) {
            if (courseApplication.getProgress() != CourseApplication.PROGRESS_PAID) {
                continue;
            }
            Course course = entityDao.get("course", courseApplication.getCourseId(), CourseRowMapper.getInstance());
            if (course != null) {
                setCourseProperties(course);
                courses.add(course);
            }
        }
        request.setAttribute("courses", courses);
        request.setAttribute("student", user);

        return Response.ok(new Viewable("student")).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response index() {
        User user = getSessionUser();
        if (user == null) {
            return signinAndGoback();
        }

        if (user.isInstructor()) {
            return redirect("/courses/as_an_instructor");
        }

        return redirect("/courses/as_a_student");
    }

    private void setCourseProperties(Course course) {
        course.setDescription(StringUtil.replaceNewLine(course.getDescription()));
        course.setContent(StringUtil.replaceNewLine(course.getContent()));

        if (course.getInstructorId() > 0) {
            Instructor instructor = entityDao.get(Instructor.SQL_TABLE_NAME, course.getInstructorId(), InstructorRowMapper.getInstance());
            if (instructor != null) {
                course.getProperties().put("instructor", instructor);
            }
        }
    }
}
