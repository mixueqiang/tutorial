package com.yike.service;

import com.yike.model.WxMessage;
import com.yike.task.WxServiceScheduler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author ilakeyc
 * @since 2017/2/14
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:ApplicationContext.xml")
public class WxServiceTest {

    @Resource
    WxITService wxITService;
    @Resource
    WxYiKeService wxYiKeService;
    @Resource
    WxServiceScheduler wxServiceScheduler;

    @Test
    public void testYiKeApplicationButton() {
        wxServiceScheduler.setupWxResponses();

        WxMessage message = new WxMessage();
        message.setContent("?");

    }



}