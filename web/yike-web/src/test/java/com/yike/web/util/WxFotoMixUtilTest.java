package com.yike.web.util;

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

//        apiUtils.requestAccessToken();
//
//        apiUtils.sendTextMessage("[机智]请先填写入学资料，然后添加小编微信等待拉你入群哦~\n微信号：lenkasummer\n", "o6_3dwGJWBSj0eK6LJdBQRnYSCIY");
//        File image = WxFotoMixUtils.getZhoumoQrCode();
//        String mediaId = apiUtils.uploadTempImage(image);
//        apiUtils.sendImageMessage(mediaId, "o6_3dwGJWBSj0eK6LJdBQRnYSCIY");

//        WxUser user = wxITUserService.getUser("o6_3dwBs0gJzZdOh8XhPASLPBk-s");
//
//        if (StringUtils.isEmpty(user.getQrTicket())) {
//            String ticket = WxITService.apiUtils.requestQRCode(user.getOpenid());
//            user.setQrTicket(ticket);
//        }
//
//        WxFotoMixUtils.createInvitationImage(user, user.getQrTicket());
    }

}