package com.yike.service;

import com.yike.Constants;
import com.yike.dao.EntityDao;
import com.yike.model.Entity;
import com.yike.model.WxMessage;
import com.yike.model.WxUser;
import com.yike.web.util.WxApiUtils;
import com.yike.web.util.WxFotoMixUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class WxService {
  private static final Log LOG = LogFactory.getLog(WxService.class);

  private static ExecutorService executor = Executors.newFixedThreadPool(10);

  @Resource
  protected EntityDao entityDao;

  public boolean handleMessage(WxMessage wxMessage) {

    LOG.info("MSG EVENT : " + wxMessage.getEvent());
    //文本消息
    if ("text".equals(wxMessage.getMsgType())) {
      return handleTextMsg(wxMessage);
    }
    if ("event".equals(wxMessage.getMsgType())) {
      return handleEventMsg(wxMessage);
    }

    return true;
  }

  private boolean handleEventMsg(WxMessage wxMessage) {
    //点击
    if ("CLICK".equals(wxMessage.getEvent())) {
      return handleClickMsg(wxMessage);
    }
    //取消关注
    if ("unsubscribe".equals(wxMessage.getEvent())) {
      return handleUnsubscribeMsg(wxMessage);
    }
    //关注
    if ("subscribe".equals(wxMessage.getEvent())) {
      return handleSubscribeMsg(wxMessage);
    }
    return true;
  }

  private boolean handleSubscribeMsg(WxMessage wxMessage) {
    // TODO Auto-generated method stub
    return true;
  }

  private boolean handleUnsubscribeMsg(WxMessage wxMessage) {
    // TODO Auto-generated method stub
    return true;
  }

  private boolean handleClickMsg(WxMessage wxMessage) {
    if ("com.yikeshangshou.wx.share.plan".equals(wxMessage.getEventKey())) {

    }
    if ("com.yikeshangshou.wx.free".equals(wxMessage.getEventKey())) {
      return handleFreeClickEvent(wxMessage);
    }

    return true;
  }

  private boolean handleFreeClickEvent(final WxMessage message) {

    final WxUser user = WxApiUtils.requestWxUser(message.getFromUserName());
    if (user == null) {
      return false;
    }
    saveUser(user, message.getFromUserName());
    WxApiUtils.sendTextMessage("滴~  学生卡 (*￣▽￣*) \n\n"
            + "限时名额有限，请在1小时内将下方专属邀请卡发送朋友圈或群哦~ \n\n"
            + "Ps:（完成 2 个朋友扫码支持，系统会自动给您发送入学通知）\n\n"
            + "↓↓邀请卡正在生成中↓↓", message.getFromUserName());
    executor.execute(new Runnable() {
      public void run() {
        if (!sendInvitationImage(user, message)) {
          WxApiUtils.sendTextMessage("图片生成失败，请稍后再试。", message.getFromUserName());
        }
      }
    });
    return true;
  }

  private boolean sendInvitationImage(WxUser user, WxMessage message) {

    String ticket = WxApiUtils.requestQRCode(user.getOpenid());

    File image = WxFotoMixUtils.createInvitationImage(user, ticket);

    if (image == null) {
      WxApiUtils.sendTextMessage("图片生成失败，请稍后再试。", message.getFromUserName());
      return false;
    }

    String mediaId = WxApiUtils.uploadTempImage(image);

    if (StringUtils.isEmpty(mediaId)) {
      WxApiUtils.sendTextMessage("图片生成失败，请稍后再试。", message.getFromUserName());
      return false;
    }

    return WxApiUtils.sendImageMessage(mediaId, message.getFromUserName());
  }


  private boolean handleTextMsg(WxMessage wxMessage) {
    WxApiUtils.sendTextMessage("消息已收到，暂无关于" + wxMessage.getContent() + "的回复", wxMessage.getFromUserName());
    return true;
  }


  private void saveUser(WxUser user, String openId) {
    Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
    wxUserFindCondition.put("openId", openId);
    if (entityDao.exists("wx_user", wxUserFindCondition)) {
      return;
    }
    long createTime = System.currentTimeMillis();
    Entity entity = new Entity("wx_user");
    entity.set("createTime", createTime);
    entity.set("subscribe", user.getSubscribe());
    entity.set("openid", user.getOpenid()).set("openId", openId);
    if (!"0".equals(user.getSubscribe())) {
      entity.set("nickName", user.getNickname());
      entity.set("sex", user.getSex());
      entity.set("language", user.getLanguage());
      entity.set("city", user.getCity());
      entity.set("province", user.getProvince());
      entity.set("country", user.getCountry());
      entity.set("headImgUrl", user.getHeadimgurl());
      entity.set("subscribeTime", user.getSubscribe_time() * 1000);
      entity.set("unionid", user.getUnionid());
      entity.set("remark", user.getRemark());
      entity.set("groupid", user.getGroupid());
      entity.set("status", Constants.STATUS_OK);
    }
    try {
      entityDao.save(entity);
    } catch (Throwable t) {
      LOG.error("WxUser save to database failure", t);
    }
  }


}
