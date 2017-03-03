package com.yike.web.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.view.Viewable;
import com.yike.Constants;
import com.yike.dao.CourseDao;
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.dao.mapper.CourseScheduleRowMapper;
import com.yike.model.Course;
import com.yike.model.CourseSchedule;
import com.yike.model.Entity;
import com.yike.util.PageNumberUtils;
import com.yike.util.Pair;
import com.yike.util.ResponseBuilder;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
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

    private static final Log LOG = LogFactory.getLog(AdminCourseScheduleResource.class);

    @Resource
    protected CourseDao courseDao;

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

    @POST
    @Produces(APPLICATION_JSON)
    public Map<String, Object> reset(
            @FormParam("courseId") long courseId,
            @FormParam("date") String date,
            @FormParam("time") String time,
            @FormParam("daysOfWeek") String daysOfWeek,
            @FormParam("totalCount") int totalCount) {

        if (courseId <= 0) {
            return ResponseBuilder.error(90000, "课程id不合法。");
        }
        if (StringUtils.isEmpty(date)) {
            return ResponseBuilder.error(90001, "开始日期不能为空。");
        }
        if (StringUtils.isEmpty(time)) {
            return ResponseBuilder.error(90002, "开始时间不能为空。");
        }
        if (totalCount <= 0) {
            return ResponseBuilder.error(90003, "总课时不能0。");
        }

        Gson gson = new Gson();
        List<String> days = null;
        try {
            days = gson.fromJson(daysOfWeek, new TypeToken<List<String>>() {
            }.getType());
        } catch (Throwable t) {
            LOG.error("foo", t);
        }

        boolean flag = courseDao.setCourseSchedules(courseId, date, time, days, totalCount);

        if (flag) {
            return ResponseBuilder.OK;
        } else {
            return ResponseBuilder.error(50000, "失败，请查看错误日志。");
        }
    }

    @POST
    @Path("edit")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> edit(
            @FormParam("scheduleId") long scheduleId,
            @FormParam("date") String date,
            @FormParam("time") String time) {

        if (scheduleId <= 0) {
            return ResponseBuilder.error(90000, "课程表id不合法。");
        }
        if (StringUtils.isEmpty(date)) {
            return ResponseBuilder.error(90001, "开始日期不能为空。");
        }
        if (StringUtils.isEmpty(time)) {
            return ResponseBuilder.error(90002, "开始时间不能为空。");
        }

        try {

            Map<String, Object> existCondition = new HashMap<String, Object>();
            existCondition.put("id", scheduleId);
            existCondition.put("launchData", date);
            existCondition.put("launchTime", time);
            existCondition.put("status", Constants.STATUS_OK);
            if (entityDao.exists("course_schedule", existCondition)) {
                return ResponseBuilder.error(90003, "该日程已存在。");
            }

            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("id", scheduleId);
            Map<String, Object> updateValues = new HashMap<String, Object>();
            updateValues.put("launchDate", date);
            updateValues.put("launchTime", time);
            updateValues.put("status", Constants.STATUS_OK);
            entityDao.update("course_schedule", condition, updateValues);
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            LOG.error("Update course_schedule failure", t);
            return ResponseBuilder.error(t);
        }
    }

    @POST
    @Path("add")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> add(
            @FormParam("courseId") long courseId,
            @FormParam("date") String date,
            @FormParam("time") String time) {

        if (courseId <= 0) {
            return ResponseBuilder.error(90000, "课程表id不合法。");
        }
        if (StringUtils.isEmpty(date)) {
            return ResponseBuilder.error(90001, "开始日期不能为空。");
        }
        if (StringUtils.isEmpty(time)) {
            return ResponseBuilder.error(90002, "开始时间不能为空。");
        }


        try {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("courseId", courseId);
            condition.put("launchData", date);
            condition.put("launchTime", time);
            condition.put("status", Constants.STATUS_OK);
            if (entityDao.exists("course_schedule", condition)) {
                return ResponseBuilder.error(90003, "该日程已存在。");
            }
            Entity entity = new Entity("course_schedule");
            entity.set("courseId", courseId)
                    .set("launchDate", date)
                    .set("launchTime", time)
                    .set("createTime", System.currentTimeMillis())
                    .set("status", Constants.STATUS_OK);
            entityDao.save(entity);
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            LOG.error("Update course_schedule failure", t);
            return ResponseBuilder.error(t);
        }
    }

    @POST
    @Path("delete")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> delete(
            @FormParam("scheduleId") long scheduleId) {

        if (scheduleId <= 0) {
            return ResponseBuilder.error(90000, "课程表id不合法。");
        }

        try {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("id", scheduleId);
            Map<String, Object> updateValues = new HashMap<String, Object>();
            updateValues.put("status", Constants.STATUS_NO);
            entityDao.update("course_schedule", condition, updateValues);
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            LOG.error("Delete course_schedule failure", t);
            return ResponseBuilder.error(t);
        }
    }
}
