package com.yike.service;

import com.yike.model.WxMessage;
import com.yike.web.util.WxApiUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author ilakeyc
 * @since 2017/2/14
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:ApplicationContext.xml")
public class WxServiceTest {

  @Resource
  WxService wxService;

  @Test
  public void mainTest() {
    WxApiUtils.requestAccessToken();

    WxMessage message = new WxMessage();

    message.setFromUserName("o6_3dwOk_aYvpSE8LpGatjpYtwzE");
    message.setMsgType("event");
    message.setEvent("subscribe");
    message.setTicket("gQHq8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAySUt4TVoySFpmdDAxMDAwMDAwN0IAAgRYk6FYAwQAAAAA");

    wxService.handleMessage(message);

  }

}