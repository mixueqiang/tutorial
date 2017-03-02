package com.yike.task;

import com.yike.dao.CourseDao;
import com.yike.model.Course;
import com.yike.model.CourseApplication;
import com.yike.model.CourseSchedule;
import com.yike.model.WxUser;
import com.yike.service.WxYiKeService;
import com.yike.service.impl.WxYiKeUserServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/3/2
 */

@Service
public class WxYikeNotificationScheduler implements Runnable {

    private static final Log LOG = LogFactory.getLog(WxYikeNotificationScheduler.class);

    public WxYikeNotificationScheduler() {
        TaskScheduler.register(getClass().getSimpleName(), this, 40, 30);
    }

    @Resource
    protected CourseDao courseDao;

    @Resource
    protected WxYiKeUserServiceImpl wxYiKeUserService;

    @Resource
    protected WxYiKeService wxYiKeService;

    @Override
    public void run() {
        postCourseOnLaunchNotification();
    }

    private void postCourseOnLaunchNotification() {
        Map<String, CourseSchedule> courseSchedules = CourseSchedulerObserver.getSchedules();
        for (String key : courseSchedules.keySet()) {
            CourseSchedule courseSchedule = courseSchedules.get(key);
            String launchDate = courseSchedule.getLaunchDate();
            String launchTime = courseSchedule.getLaunchTime();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                long launchTimeMillis = dateFormatter.parse(launchDate + " " + launchTime).getTime();
                long currentTimeMillis = System.currentTimeMillis();
                if (launchTimeMillis - currentTimeMillis < 3600000) {
                    long courseId = courseSchedule.getCourseId();
                    Course course = courseDao.getCourse(courseId);
                    if (course == null) {
                        continue;
                    }
                    List<CourseApplication> courseApplications = courseDao.getCourseApplication(courseId);
                    if (courseApplications == null || courseApplications.isEmpty()) {
                        continue;
                    }

                    for (CourseApplication courseApplication : courseApplications) {

                        String name = courseApplication.getName();
                        long userId = courseApplication.getUserId();
                        String courseName = course.getName();
                        WxUser wxUser = wxYiKeUserService.findByUserId(userId);
                        if (wxUser == null) {
                            continue;
                        }
                        String openId = wxUser.getOpenid();
                        if (!wxYiKeService.sendCourseOnLaunchNoticeTemplateMessage(
                                courseId,
                                openId,
                                name,
                                courseName,
                                courseSchedule.getLaunchDate() + " " + courseSchedule.getLaunchTime())) {
                            LOG.error("Template message post failure with user id : " + String.valueOf(userId) + " course id : " + String.valueOf(courseId));
                        }
                    }

                }
            } catch (ParseException e) {
                e.printStackTrace();
                LOG.error("Formatting launchTime string to launchTimeMillis failure", e);
            }

            CourseSchedulerObserver.getSchedules().remove(String.valueOf(courseSchedule.getId()));
        }
    }
}
