package com.yike.service;

import com.yike.model.WxMessage;
import com.yike.model.WxUser;
import com.yike.service.impl.WxITUserServiceImpl;
import com.yike.web.util.WxApiUtils;
import com.yike.web.util.WxFotoMixUtils;
import com.yike.web.util.WxTemplateMessageFormatter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ilakeyc
 * @since 10/02/2017
 */
@Service
public class WxITService {
    private static final Log LOG = LogFactory.getLog(WxITService.class);

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    private static String NEW_MEMBER_JOINING_NOTICE_TEMPLATE_ID = "-nKMTybLcXHNsle6Ib5mdpO4ec2xFpCW6zjMX6mPzNQ";
    private static String TASK_COMPLETION_NOTICE_TEMPLATE_ID = "G66-ZH08CQdAZVKWZH3do_pqwqaTDk8AKx_9QNzm1hg";

    public static String WX_TOKEN = "yikeshangshouwx";
    public static String APP_ID = "wxce4aa0af6d3ec704";
    public static String APP_SECRET = "5f8238027cab1b5348df2dd86f5bd6fe";

    @Resource
    private WxITUserService wxUserService;

    public static WxApiUtils apiUtils = new WxApiUtils(APP_ID, APP_SECRET);

    private static Map<String, String> createdInvitationImageUsers = new HashMap<String, String>();

    public boolean handleMessage(WxMessage message) {

        LOG.info("MSG EVENT : " + message.getEvent());
        //文本消息
        if ("text".equals(message.getMsgType())) {
            return handleTextMsg(message);
        }
        if ("event".equals(message.getMsgType())) {
            return handleEventMsg(message);
        }

        return true;
    }

    private boolean handleEventMsg(WxMessage message) {
        //点击
        if ("CLICK".equals(message.getEvent())) {
            return handleClickMsg(message);
        }
        //取消关注
        if ("unsubscribe".equals(message.getEvent())) {
            return handleUnsubscribeMsg(message);
        }
        //关注
        if ("subscribe".equals(message.getEvent())) {
            return handleSubscribeMsg(message);
        }
        // 扫描二维码
        if ("SCAN".equals(message.getEvent())) {
            return handleScanMsg(message);
        }
        return true;
    }

    private boolean handleSubscribeMsg(WxMessage message) {

        wxUserService.sync(message.getFromUserName());

        if (isInvitationEvent(message)) {
            return handleInvitationEvent(message);
        }

        return true;
    }

    private boolean handleUnsubscribeMsg(WxMessage message) {
        wxUserService.sync(message.getFromUserName());
        return true;
    }

    private boolean handleClickMsg(WxMessage message) {
        if ("com.yikeshangshou.wx.share.web".equals(message.getEventKey())) {
            return handleWebShareClientEvent(message);
        }
        if ("com.yikeshangshou.wx.share.java".equals(message.getEventKey())) {
            return handleJavaShareClientEvent(message);
        }
        if ("com.yikeshangshou.wx.share.db".equals(message.getEventKey())) {
            return handleDBShareClientEvent(message);
        }
        if ("com.yikeshangshou.wx.source".equals(message.getEventKey())) {
            return handleSourceClickEvent(message);
        }
        if ("com.yikeshangshou.wx.free".equals(message.getEventKey())) {
            return handleFreeClickEvent(message);
        }

        return true;
    }

    private boolean handleDBShareClientEvent(WxMessage message) {
        sendStudentMessage(message.getFromUserName(), "数据资料地址");
        return false;
    }

    private boolean handleJavaShareClientEvent(WxMessage message) {
        sendStudentMessage(message.getFromUserName(), "Java资料地址");
        return false;
    }

    private boolean handleWebShareClientEvent(WxMessage message) {
        sendStudentMessage(message.getFromUserName(), "前端资料地址");
        return false;
    }

    private boolean handleSourceClickEvent(WxMessage message) {
        sendStudentMessage(message.getFromUserName(), "https://github.com/yikeshangshou/boluo");
        return false;
    }

    private void sendStudentMessage(String openId, String eventMsg) {
        if (wxUserService.isStudent(openId)) {
            apiUtils.sendTextMessage(eventMsg, openId);
        } else {
            apiUtils.sendTextMessage("你还未入学，请先点击免费入学完成入学，再进行后续操作", openId);
        }
    }

    private boolean handleScanMsg(WxMessage message) {
        if (isInvitationEvent(message)) {
            return handleInvitationEvent(message);
        }
        return true;
    }

    private boolean handleTextMsg(WxMessage wxMessage) {
        apiUtils.sendTextMessage("消息已收到，暂无关于" + wxMessage.getContent() + "的回复", wxMessage.getFromUserName());
        return true;
    }

    private boolean isInvitationEvent(WxMessage message) {
        String qrTicket = message.getTicket();
        return wxUserService.hasTicket(qrTicket);
    }

    private boolean handleInvitationEvent(WxMessage message) {

        WxUser invter;
        String scannedOpenId = message.getFromUserName();
        String invterOpenId = null;
        String ticket = message.getTicket();
        invter = wxUserService.findByTicket(ticket);

        if (invter == null) {
            LOG.error("Not found source WxUser with qeTicket : " + ticket);
            return false;
        }

        invterOpenId = invter.getOpenid();

        if (StringUtils.equals(invterOpenId, scannedOpenId)) {
            // 自己不能邀请自己
            return false;
        }
        WxUser scannedUser = wxUserService.getUser(scannedOpenId);
        if (scannedUser == null) {
            return false;
        }
        if (0 == scannedUser.getId()) {
            LOG.error("Not found scannedUser in database or save failure.");
            return false;
        }
        if (!wxUserService.saveInvitation(scannedOpenId, invterOpenId)) {
            return false;
        }
        if (invter.getIsStudent() == 1) {
            return true;
        }
        int count = wxUserService.countInvitation(invterOpenId);

        if (count == 1) {
            return sendInvitingTemplateMessage(scannedUser, invter);
        }
        if (count == 2) {
            if (wxUserService.makeStudent(invterOpenId)) {
                return sendFreeAdmissionTemplateMessage(invter);
            }
        }

        return true;
    }

    private boolean handleFreeClickEvent(final WxMessage message) {
        final String openId = message.getFromUserName();

        String lastTimeMillisStr = createdInvitationImageUsers.get(openId);
        long currentTimeMillis = System.currentTimeMillis();
        if (StringUtils.isNotEmpty(lastTimeMillisStr)) {
            long lastTimeMillis = Long.parseLong(lastTimeMillisStr);
            long difference = 60 - (currentTimeMillis - lastTimeMillis) / 1000;
            if (difference > 0) {
                return apiUtils.sendTextMessage(String.valueOf(difference) + "秒之后再来吧！", message.getFromUserName());
            }
        }
        createdInvitationImageUsers.put(openId, String.valueOf(currentTimeMillis));

        apiUtils.sendTextMessage("滴~  学生卡 (*￣▽￣*) \n\n"
                + "限时名额有限，请在1小时内将下方专属邀请卡发送朋友圈或群哦~ \n\n"
                + "Ps:（完成 2 个朋友扫码支持，系统会自动给您发送入学通知）\n\n"
                + "↓↓邀请卡正在生成中↓↓", openId);
        executor.execute(new Runnable() {
            public void run() {
                WxUser user = wxUserService.getUser(openId);
                if (user != null) {
                    sendInvitationImage(user);
                } else {
                    apiUtils.sendTextMessage("图片生成失败，请稍后再试。", openId);
                }
            }
        });
        return true;
    }

    private boolean sendInvitationImage(WxUser user) {
        File image = null;
        String ticket = user.getQrTicket();
        String openId = user.getOpenid();

        if (StringUtils.isNotEmpty(ticket)) {
            image = WxFotoMixUtils.localInvitationImage(ticket);
        } else {
            ticket = apiUtils.requestQRCode(openId);
        }

        if (image == null) {
            image = WxFotoMixUtils.createInvitationImage(user, ticket);
        }

        if (image == null) {
            apiUtils.sendTextMessage("图片生成失败，请稍后再试。", openId);
            return false;
        }

        wxUserService.saveTicket(openId, ticket);
        String mediaId = apiUtils.uploadTempImage(image);

        if (StringUtils.isEmpty(mediaId)) {
            apiUtils.sendTextMessage("图片生成失败，请稍后再试。", openId);
            return false;
        }

        return apiUtils.sendImageMessage(mediaId, openId);
    }

    private boolean sendInvitingTemplateMessage(WxUser scanner, WxUser inviter) {
        Map<String, Object> data = WxTemplateMessageFormatter.formateNewMemberNotice(scanner.getNickname());
        return apiUtils.sendTemplateMessage(NEW_MEMBER_JOINING_NOTICE_TEMPLATE_ID, null, data, inviter.getOpenid());
    }

    private boolean sendFreeAdmissionTemplateMessage(WxUser toUser) {
        Map<String, Object> data = WxTemplateMessageFormatter.formateTaskCompletionNotice();
        return apiUtils.sendTemplateMessage(TASK_COMPLETION_NOTICE_TEMPLATE_ID, "https://www.sojump.hk/jq/12131778.aspx", data, toUser.getOpenid());
    }
}
