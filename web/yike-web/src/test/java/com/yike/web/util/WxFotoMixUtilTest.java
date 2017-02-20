package com.yike.web.util;

import com.yike.model.WxUser;
import com.yike.service.WxITService;
import com.yike.service.WxITUserService;
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
public class WxFotoMixUtilTest {

  @Resource
  WxITUserService wxITUserService;

  @Test
  public void createInvitationImage() throws Exception {
    WxITService.apiUtils.requestAccessToken();

    WxUser user = wxITUserService.getUser("o6_3dwOk_aYvpSE8LpGatjpYtwzE");

//    WxUser user = new WxUser();
//    user.setNickname("周陌");
//    user.setOpenid("o6_3dwEc_-lPFEAODb7_J91z80_M");
//    user.setHeadimgurl("http://wx.qlogo.cn/mmopen/ajNVdqHZLLDdbj0pN9jv5lnerZS8lSUyXYawecUicNF2t0d2u6mXw0ibXicYj8Gbkut8boSjmwQ1mNfTNWnuxXJgA/0");
//    String ticket = WxITService.apiUtils.requestQRCode(user.getOpenid());
    if (user != null) {
      user.setNickname("a测b试c测d试e");
      WxFotoMixUtils.createInvitationImage(user, user.getQrTicket());
    } else {
      System.out.println("没有找到用户");
    }
  }

}