package com.yike.service;

import com.yike.model.WxMessage;
import com.yike.web.util.WxApiUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ilakeyc
 * @since 2017/2/17
 */
@Service
public class WxYiKeService {
    private static final Log LOG = LogFactory.getLog(WxYiKeService.class);

    public static String WX_TOKEN = "yikeshangshouwx";
    public static String APP_ID = "wx19882be1b89e09e5";
    public static String APP_SECRET = "1b9fc8a682a840147a777822c14471f0";

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

        if ("com.yikeshangshou.wx.about".equals(message.getEventKey())) {
            return handleAboutClickEvent(message);
        }

        return true;
    }

    private boolean handleScanMsg(WxMessage message) {
        return true;
    }

    private boolean handleTextMsg(WxMessage wxMessage) {
        return apiUtils.sendTextMessage("消息已收到，暂无关于" + wxMessage.getContent() + "的回复", wxMessage.getFromUserName());
    }

    private boolean handleBindingClickEvent(WxMessage message) {
        
        return true;
    }

    private boolean handleApplicationClickEvent(WxMessage message) {
        return true;
    }

    private boolean handleAboutClickEvent(WxMessage message) {
        return apiUtils.sendTextMessage(
                "「一课上手」 专注于实践课程的在线平台。一课上手致力于把行业专家的一线实战带给学员，让学员在实战中把学到的东西快速上手。术业专攻，「一课上手」。\n\n 课程和更多信息请访问：http://www.dabllo.com/", message.getFromUserName());
    }

}
