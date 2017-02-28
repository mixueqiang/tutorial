package com.yike.web.util;

import com.yike.model.WxUser;
import com.yike.service.WxITService;
import com.yike.service.WxITUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

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

        WxUser user = wxITUserService.getUser("o6_3dwBs0gJzZdOh8XhPASLPBk-s");

        if (StringUtils.isEmpty(user.getQrTicket())) {
            String ticket = WxITService.apiUtils.requestQRCode(user.getOpenid());
            user.setQrTicket(ticket);
        }

        WxFotoMixUtils.createInvitationImage(user, user.getQrTicket());
    }

}