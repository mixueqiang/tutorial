package com.yike.dao;

import com.yike.Constants;
import com.yike.dao.mapper.CourseApplicationRowMapper;
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.dao.mapper.CourseScheduleRowMapper;
import com.yike.model.Course;
import com.yike.model.CourseApplication;
import com.yike.model.CourseSchedule;
import com.yike.model.Entity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ilakeyc
 * @since 2017/3/2
 */

@Repository
public class CourseDao {
    private static final Log LOG = LogFactory.getLog(CourseDao.class);

    @Resource
    protected EntityDao entityDao;

    /**
     * @param courseId        课程的ID
     * @param beginDateStr    开始上课日期：yyyy-MM-dd
     * @param launchTimeStr   每次上课时间：HH:mm
     * @param dayOfWeekStrs   每周哪些天上课，如周日，周一，周二：[1,2,3]
     * @param totalClassCount 总课时
     * @return 是否保存成功
     */
    public boolean setCourseSchedules(long courseId, String beginDateStr, String launchTimeStr, List<String> dayOfWeekStrs, int totalClassCount) {
        if (courseId == 0 || StringUtils.isEmpty(beginDateStr) || StringUtils.isEmpty(launchTimeStr) || totalClassCount == 0) {
            return false;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date launchDate = null;
        try {
            launchDate = dateFormatter.parse(beginDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            LOG.error("Unable formate " + beginDateStr + " to Date", e);
            return false;
        }
        long launchDateMillis = launchDate.getTime();
        long nowTimeMillis = System.currentTimeMillis();

        setAllScheduleToNotReady(courseId);

        do {

            launchDate.setTime(launchDateMillis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(launchDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeekStrs == null || dayOfWeekStrs.isEmpty() || dayOfWeekStrs.contains(String.valueOf(dayOfWeek))) {

                beginDateStr = dateFormatter.format(launchDate);
                totalClassCount -= 1;

                if (!enableScheduleIfExist(courseId, beginDateStr, launchTimeStr)) {

                    Entity entity = new Entity("course_schedule");
                    entity.set("courseId", courseId)
                            .set("status", Constants.STATUS_READY)
                            .set("launchDate", beginDateStr)
                            .set("launchTime", launchTimeStr)
                            .set("createTime", nowTimeMillis);

                    try {
                        entityDao.save(entity);
                    } catch (Throwable t) {
                        LOG.error("Unable to save course schedule at launch date : " +
                                beginDateStr +
                                " courseId : " +
                                String.valueOf(courseId));
                    }

                }

            }

            launchDateMillis += 86400000L;

        } while (totalClassCount > 0);

        return true;
    }

    public List<CourseSchedule> getTodaySchedules() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("launchDate", today);
        condition.put("status", Constants.STATUS_OK);
        return entityDao.find("course_schedule", condition, CourseScheduleRowMapper.getInstance());
    }

    public List<CourseSchedule> getTodaySchedules(long courseId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("launchDate", today);
        condition.put("courseId", courseId);
        condition.put("status", Constants.STATUS_OK);
        return entityDao.find("course_schedule", condition, CourseScheduleRowMapper.getInstance());
    }

    private boolean scheduleExist(long courseId, String dateStr, String timeStr) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("launchDate", dateStr);
        condition.put("launchTime", timeStr);
        condition.put("courseId", courseId);
        condition.put("status", Constants.STATUS_READY);
        return entityDao.exists("course_schedule", condition);
    }

    private boolean enableScheduleIfExist(long courseId, String dateStr, String timeStr) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("launchDate", dateStr);
        condition.put("launchTime", timeStr);
        condition.put("courseId", courseId);
        if (entityDao.exists("course_schedule", condition)) {
            try {
                entityDao.update("course_schedule", condition, "status", Constants.STATUS_READY);
                return true;
            } catch (Throwable t) {
                LOG.error("Course schedule exist. But set status to READY failure", t);
                return false;
            }
        }
        return false;
    }

    public boolean setScheduleToNotReady(long scheduleId) {
        try {
            entityDao.update("course_schedule", "id", scheduleId, "status", Constants.STATUS_NOT_READY);
            return true;
        } catch (Throwable t) {
            LOG.error("Set all schedule to NOT_READY failure", t);
            return false;
        }
    }

    private boolean setAllScheduleToNotReady(long courseId) {
        try {
            entityDao.update("course_schedule", "courseId", courseId, "status", Constants.STATUS_NOT_READY);
            return true;
        } catch (Throwable t) {
            LOG.error("Set all schedule to NOT_READY failure", t);
            return false;
        }
    }

    public List<CourseApplication> getCourseApplication(long courseId) {
        try {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("courseId", courseId);
            condition.put("status", Constants.STATUS_OK);
            condition.put("progress", CourseApplication.PROGRESS_PAID);
            return entityDao.find(
                    "course_application",
                    condition,
                    CourseApplicationRowMapper.getInstance());
        } catch (Throwable t) {
            LOG.error("Find course application failure with course id : " + String.valueOf(courseId));
            return null;
        }
    }

    public Course getCourse(long courseId) {
        try {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("id", courseId);
            condition.put("status", Constants.STATUS_OK);

            return entityDao.findOne("course", condition, CourseRowMapper.getInstance());

        } catch (Throwable t) {
            LOG.error("Find course by id failure with course id : " + String.valueOf(courseId), t);
            return null;
        }
    }
}
