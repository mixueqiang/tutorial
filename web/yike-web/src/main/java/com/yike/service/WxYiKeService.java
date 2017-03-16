package com.yike.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yike.model.User;
import com.yike.model.WxMessage;
import com.yike.model.WxUser;
import com.yike.web.util.WxApiUtils;
import com.yike.web.util.WxFotoMixUtils;
import com.yike.web.util.WxTemplateMessageFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/2/17
 */
@Service
public class WxYiKeService {
    private static final Log LOG = LogFactory.getLog(WxYiKeService.class);

    private static final String BINDING_STATUS_NOTICE_TEMPLATE_ID = "801LSYIv8DB_bN8d8bHg7zY-3kXRV7Tq8yH615Wse3Y";
    private static final String BINDING_SUCCESS_NOTICE_TEMPLATE_ID = "5DjnVwY1FomxQhvd7kiIn0DmMOX6LmaBrdgW1mcAGGE";
    private static final String COURSE_ON_LAUNCH_NOTICE_TEMPLATE_ID = "UER4FNumEnvJlvRU7fjnF77N6_dnWJmvhkhhi0mXP_c";

    public static String WX_TOKEN = "yikeshangshouwx";
    public static String APP_ID = "wxf67017b21672abff";
    public static String APP_SECRET = "64510b96c537ac4563d2619919540df4";

    @Resource
    public WxYiKeUserService wxUserService;

    public static WxApiUtils apiUtils = new WxApiUtils(APP_ID, APP_SECRET);

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

        return true;
    }

    private boolean handleUnsubscribeMsg(WxMessage message) {
        wxUserService.sync(message.getFromUserName());

        return true;
    }

    private boolean handleClickMsg(WxMessage message) {

        if ("com.yikeshangshou.wx.binding".equals(message.getEventKey())) {
            return handleBindingClickEvent(message);
        }

        if ("com.yikeshangshou.wx.application".equals(message.getEventKey())) {
            return handleApplicationClickEvent(message);
        }

        if ("com.yikeshangshou.wx.download".equals(message.getEventKey())) {
            return handleAboutClickEvent(message);
        }

        return true;
    }

    private boolean handleScanMsg(WxMessage message) {
        return true;
    }

    private boolean handleTextMsg(WxMessage message) {
        return apiUtils.sendTextMessage("消息已收到，暂无关于" + message.getContent() + "的回复", message.getFromUserName());
    }

    private boolean handleBindingClickEvent(WxMessage message) {
        WxUser wxUser = wxUserService.getUser(message.getFromUserName());
        if (wxUser == null) {
            return false;
        }
        User user = wxUserService.getBindingUser(wxUser);
        if (user == null || StringUtils.isEmpty(user.getPhone())) {
            return sendUnboundNoticeTemplateMessage(wxUser.getOpenid());
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return sendBindingPasswordNoticeTemplateMessage(wxUser.getOpenid(), user.getPhone());
        }
        return sendHasBindingNoticeTemplateMessage(wxUser.getOpenid(), user.getUsername(), user.getPhone());
    }

    private boolean handleApplicationClickEvent(WxMessage message) {
        WxUser wxUser = wxUserService.getUser(message.getFromUserName());
        if (wxUser == null) {
            return false;
        }
        User user = wxUserService.getBindingUser(wxUser);
        if (user == null) {
            return sendUnboundNoticeTemplateMessage(wxUser.getOpenid());
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return sendBindingPasswordNoticeTemplateMessage(wxUser.getOpenid(), user.getPhone());
        }

        List<String> courseNames = wxUserService.getApplicationCourseNames(wxUser);

        boolean hasApplication = courseNames != null && !courseNames.isEmpty();

        if (hasApplication) {

            Gson g = new GsonBuilder().disableHtmlEscaping().create();
            String courseNameStr = g.toJson(courseNames);
            courseNameStr = courseNameStr.replace("[", "\n");
            courseNameStr = courseNameStr.replace("]", "\n");
            if (apiUtils.sendTextMessage("你现在报名了：" + courseNameStr + "请添加小编，将此页截图发给小编，等待课程分班，拉你入群。", wxUser.getOpenid())) {

                String mediaId = apiUtils.uploadTempImage(WxFotoMixUtils.getEditorQrCode());
                if (org.apache.commons.lang.StringUtils.isEmpty(mediaId)) {
                    return apiUtils.sendTextMessage("哎呀！发送二维码失败啦~~再试一次吧~~", wxUser.getOpenid());
                }
                return apiUtils.sendImageMessage(mediaId, wxUser.getOpenid());

            }

        } else {

            if (apiUtils.sendTextMessage("你现在尚未报名课程，如有疑问，请添加小编进行咨询！！！", wxUser.getOpenid())) {
                String mediaId = apiUtils.uploadTempImage(WxFotoMixUtils.getEditorQrCode());
                if (org.apache.commons.lang.StringUtils.isEmpty(mediaId)) {
                    return apiUtils.sendTextMessage("哎呀！发送二维码失败啦~~再试一次吧~~", wxUser.getOpenid());
                }
                return apiUtils.sendImageMessage(mediaId, wxUser.getOpenid());
            }
        }
        return false;
    }

    private boolean handleAboutClickEvent(WxMessage message) {
        return apiUtils.sendTextMessage(
                "法律翻译资料  https://pan.baidu.com/s/1pK8FgYR 9eef \n" +
                        "SpringBoot中文参考指南  https://pan.baidu.com/s/1c2kVnm4 8mkn \n" +
                        "Spring、SpringMVC、MyBatis实战课程视频  https://pan.baidu.com/s/1bpAFImr zh3v \n" +
                        "Bootstrap 用户界面架构  https://pan.baidu.com/s/1eRFIZmU kyww \n" +
                        "专八核心词汇精讲  https://pan.baidu.com/s/1jHPelka bbfg \n" +
                        "老友记德语版（第1-10季）  https://pan.baidu.com/s/1jHZnxPk wgg3 \n" +
                        "响应式网页设计课程视频  https://pan.baidu.com/s/1kVlwaZd ythb ", message.getFromUserName());
    }

    private boolean sendUnboundNoticeTemplateMessage(String toUserOpenId) {
        Map<String, Object> data = WxTemplateMessageFormatter.formateUnboundNotice();
        return apiUtils.sendTemplateMessage(BINDING_STATUS_NOTICE_TEMPLATE_ID, "http://www.yikeshangshou.com/wx/binding/phone?oid=" + toUserOpenId, data, toUserOpenId);
    }

    private boolean sendBindingPasswordNoticeTemplateMessage(String toUserOpenId, String phone) {
        Map<String, Object> data = WxTemplateMessageFormatter.formateBindingPasswordNotice(phone);
        return apiUtils.sendTemplateMessage(BINDING_STATUS_NOTICE_TEMPLATE_ID, "http://www.yikeshangshou.com/wx/binding/pwd?oid=" + toUserOpenId, data, toUserOpenId);
    }

    private boolean sendHasBindingNoticeTemplateMessage(String toUserOpenId, String nickName, String phone) {
        Map<String, Object> data = WxTemplateMessageFormatter.formateHasBindingNotice(phone, nickName);
        return apiUtils.sendTemplateMessage(BINDING_STATUS_NOTICE_TEMPLATE_ID, "http://www.yikeshangshou.com", data, toUserOpenId);
    }

    public boolean sendBindingSuccessNoticeTemplateMessage(String toUserOpenId, User user) {
        if (user == null || StringUtils.isEmpty(user.getPhone())) {
            LOG.error("Unable to send BindingSuccessNoticeTemplateMessage. Because the user or user's phone number dose not exist. User : "
                    + (user == null ? "null" : user.toString()));
            return false;
        }
        Map<String, Object> data = WxTemplateMessageFormatter.formateBindingSuccessNotice(user.getPhone());
        return apiUtils.sendTemplateMessage(BINDING_SUCCESS_NOTICE_TEMPLATE_ID, "http://www.yikeshangshou.com", data, toUserOpenId);
    }

    public boolean sendCourseOnLaunchNoticeTemplateMessage(long courseId, String toUserOpenId, String userName, String courseName, String beginDate) {
        if (StringUtils.isEmpty(toUserOpenId) || StringUtils.isEmpty(userName) || StringUtils.isEmpty(courseName) || StringUtils.isEmpty(beginDate)) {
            return false;
        }
        Map<String, Object> data = WxTemplateMessageFormatter.formateCourseOnLaunchNotice(userName, courseName, beginDate);
        return apiUtils.sendTemplateMessage(COURSE_ON_LAUNCH_NOTICE_TEMPLATE_ID, "http://www.yikeshangshou.com/course/" + String.valueOf(courseId), data, toUserOpenId);
    }
}
