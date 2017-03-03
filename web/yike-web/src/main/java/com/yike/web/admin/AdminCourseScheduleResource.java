package com.yike.web.admin;

import com.sun.jersey.api.view.Viewable;
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.dao.mapper.CourseScheduleRowMapper;
import com.yike.model.Course;
import com.yike.model.CourseSchedule;
import com.yike.util.PageNumberUtils;
import com.yike.util.Pair;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/3/3
 */

@Path("/admin/schedule")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AdminCourseScheduleResource extends BaseResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response index(@QueryParam("courseId") long courseId, @QueryParam("page") int page) {
        page = page > 0 ? page : 1;
        Map<String, Object> condition = new HashMap<String, Object>();
        if (courseId > 0) {
            condition.put("courseId", courseId);

            Course course = entityDao.get(
                    "course",
                    courseId,
                    CourseRowMapper.getInstance());

            request.setAttribute("course", course);
            request.setAttribute("oneCourse", true);
        }
        condition.put("status", 1);
        Pair<Integer, List<CourseSchedule>> result = entityDao.findAndCount(
                "course_schedule",
                condition,
                page,
                20,
                CourseScheduleRowMapper.getInstance());

        List<CourseSchedule> schedules = result.right;
        if (courseId == 0) {
            for (CourseSchedule schedule : schedules) {
                Course course = entityDao.get(
                        "course",
                        schedule.getCourseId(),
                        CourseRowMapper.getInstance());
                schedule.getProperties().put("course", course);
            }
        }

        request.setAttribute("schedules", schedules);

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
