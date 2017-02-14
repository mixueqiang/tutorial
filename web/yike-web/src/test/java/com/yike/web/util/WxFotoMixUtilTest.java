package com.yike.web.util;

import com.yike.model.WxUser;
import com.yike.service.WxService;
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
  protected WxService wxService;

  @Test
  public void createInvitationImage() throws Exception {
    wxService.requestAccessToken();

    WxUser user = new WxUser();
    user.setNickname("国国国国国国国国国国国国国");
    user.setOpenid("o6_3dwGJWBSj0eK6LJdBQRnYSCIY");
    user.setHeadimgurl("http://wx.qlogo.cn/mmopen/ajNVdqHZLLBRwrmQ8rAAY5vN51mtZicHhvvdXwUKzP9R8yB84ZxfZ4mvicP4Ikjv4K2JH1ZVv6DvRSozT3CSvtkA/0");
    String ticket = wxService.requestQRCode(user.getOpenid());

    WxFotoMixUtils.createInvitationImage(user, ticket);
  }

}