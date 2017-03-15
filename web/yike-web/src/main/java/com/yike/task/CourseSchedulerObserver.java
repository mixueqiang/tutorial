package com.yike.task;

import com.yike.dao.CourseDao;
import com.yike.model.CourseSchedule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/3/2
 */

@Service
public class CourseSchedulerObserver implements Runnable {

    private static final Log LOG = LogFactory.getLog(CourseSchedulerObserver.class);

    public CourseSchedulerObserver() {
        TaskScheduler.register(getClass().getSimpleName(), this, 30, 3600 * 12);
    }

    @Resource
    private CourseDao courseDao;

    public static Map<String, CourseSchedule> schedules;

    public static Map<String, CourseSchedule> getSchedules() {
        if (schedules == null) {
            schedules = new HashMap<String, CourseSchedule>();
        }
        return schedules;
    }

    @Override
    public void run() {
        LOG.info("Course Scheduler Begin Rescan");
        System.out.println("Course Scheduler Begin Rescan");

        List<CourseSchedule> scheduleList = courseDao.getTodaySchedules();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        getSchedules().clear();
        for (CourseSchedule courseSchedule : scheduleList) {
            String date = courseSchedule.getLaunchDate() + " " + courseSchedule.getLaunchTime();
            try {
                long timeMillis = dateFormatter.parse(date).getTime();
                if (timeMillis > System.currentTimeMillis()) {
                    getSchedules().put(String.valueOf(courseSchedule.getId()), courseSchedule);
                } else {
                    courseDao.setScheduleToNotReady(courseSchedule.getId());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
