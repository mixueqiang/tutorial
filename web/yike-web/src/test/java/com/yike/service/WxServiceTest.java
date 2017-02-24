package com.yike.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    @Test
    public void testYiKeApplicationButton() {
        WxYiKeService.apiUtils.requestAccessToken();
    }



}