package com.yike.dao;

import com.yike.task.CourseSchedulerObserver;
import com.yike.task.WxYikeNotificationScheduler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author ilakeyc
 * @since 2017/3/2
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:ApplicationContext.xml")
public class CourseDaoTest {

    @Resource
    CourseSchedulerObserver courseSchedulerObserver;
    @Resource
    WxYikeNotificationScheduler wxYikeNotificationScheduler;

    @Test
    public void findTodayCourse() {

        courseSchedulerObserver.run();

        wxYikeNotificationScheduler.run();
    }

}