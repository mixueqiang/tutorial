package com.yike.service;

import com.yike.model.WxMessage;
import com.yike.model.WxUser;
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

    private static final String NEW_MEMBER_JOINING_NOTICE_TEMPLATE_ID = "-nKMTybLcXHNsle6Ib5mdpO4ec2xFpCW6zjMX6mPzNQ";
    private static final String TASK_COMPLETION_NOTICE_TEMPLATE_ID = "G66-ZH08CQdAZVKWZH3do_pqwqaTDk8AKx_9QNzm1hg";

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

        WxUser user = wxUserService.sync(message.getFromUserName());
        String nickName = user.getNickname();

        apiUtils.sendTextMessage("欢迎" + nickName + "同学，加入「IT技能成长联盟」 \n" +
                "「系统学习」IT领域课程，搭建知识体系 \n" +
                "【1】社群实时提问：解决工作遇到的问题。 \n" +
                "【2】同城/地区分班，组织学习交流活动，课程、笔记分享。 \n" +
                "【3】图书推荐，图书共同阅读，大牛带着阅读学习。 \n" +
                "【4】优秀学员兼职推荐，开源项目参与推荐，知名企业实习机会推荐。 \n" +
                "【5】1000GB社员专属学习资料，视频资料。\n" +
                "\n" +
                nickName + " 同学，请选择入学方式：\n" +
                "①回复：购买\n" +
                "直接付费365元购买入学资格 \n" +
                "\n" +
                "②回复：学习\n" +
                "或点击：下方【免费入学】按钮\n" +
                "获得免费入学名额\n" +
                "_________\n" +
                "如有疑问，欢迎留言", user.getOpenid());

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
        sendStudentMessage(message.getFromUserName(), "Mongo学习资源 链接: https://pan.baidu.com/s/1i4LH0Q5 密码: vxsc\n" +
                "Redis学习资源 链接: https://pan.baidu.com/s/1gfMbMUZ 密码: 5qxx");
        return false;
    }

    private boolean handleJavaShareClientEvent(WxMessage message) {
        sendStudentMessage(message.getFromUserName(), "Spring学习资源 链接: https://pan.baidu.com/s/1sl74mEL 密码: 6s58\nNetty学习资源 链接: https://pan.baidu.com/s/1pLTmX6r 密码: uw43");
        return false;
    }

    private boolean handleWebShareClientEvent(WxMessage message) {
        sendStudentMessage(message.getFromUserName(), "Bootstrap学习资源 链接: https://pan.baidu.com/s/1hsl1XgK 密码: h7c6");
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

    private boolean handleTextMsg(WxMessage message) {
        if ("学习".equals(message.getContent())) {
            return handleFreeClickEvent(message);
        } else if ("购买".equals(message.getContent())) {
            return apiUtils.sendTextMessage("好奇宝宝(✿◡‿◡)\n" +
                    "不体验套路，怎么学习经验呢？/偷笑\n" +
                    " \n" +
                    "回复：学习 \n" +
                    "扬帆起航<(*￣▽￣*)/", message.getFromUserName());
        } else {
            return apiUtils.sendTextMessage("消息已收到，暂无关于" + message.getContent() + "的回复", message.getFromUserName());
        }
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

        if (image == null && StringUtils.isNotEmpty(ticket)) {
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
