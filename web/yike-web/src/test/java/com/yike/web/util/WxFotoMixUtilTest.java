package com.yike.web.util;

import com.yike.model.WxUser;
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
public class WxFotoMixUtilTest {

  @Test
  public void createInvitationImage() throws Exception {
    WxApiUtils.requestAccessToken();

    WxUser user = new WxUser();
    user.setNickname("A~咸菜");
    user.setOpenid("o6_3dwOk_aYvpSE8LpGatjpYtwzE");
    user.setHeadimgurl("http://wx.qlogo.cn/mmopen/zmmyJ4q2nEZMhDd1CxtcUa8aX8g79XCTCbwribvZAYqWY5kiaPya4CdgN5Mjcs2m6gvVhjYWjHfw1Y72gn17Hiclic0eUVL0A87P/0");
    String ticket = WxApiUtils.requestQRCode(user.getOpenid());

    WxFotoMixUtils.createInvitationImage(user, ticket);
  }

}