package com.yike.service;

import com.yike.model.WxMessage;
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
public class WxITServiceTest {

  @Resource
  WxITService wxITService;

  @Test
  public void mainTest() {
    WxITService.apiUtils.requestAccessToken();

    WxMessage message = new WxMessage();

    message.setFromUserName("o6_3dwOk_aYvpSE8LpGatjpYtwzE");
    message.setMsgType("event");
    message.setEvent("subscribe");
    message.setTicket("gQHq8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAySUt4TVoySFpmdDAxMDAwMDAwN0IAAgRYk6FYAwQAAAAA");

    wxITService.handleMessage(message);

  }

}