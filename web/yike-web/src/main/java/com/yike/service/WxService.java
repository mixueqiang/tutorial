package com.yike.service;

import com.yike.Constants;
import com.yike.dao.EntityDao;
import com.yike.dao.mapper.WxUserRowMapper;
import com.yike.model.Entity;
import com.yike.model.WxMessage;
import com.yike.model.WxUser;
import com.yike.web.util.WxApiUtils;
import com.yike.web.util.WxFotoMixUtils;
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
public class WxService {
  private static final Log LOG = LogFactory.getLog(WxService.class);

  private static ExecutorService executor = Executors.newFixedThreadPool(10);

  @Resource
  protected EntityDao entityDao;

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

    if (isInvitationEvent(message)) {
      return handleInvitationEvent(message);
    }

    return true;
  }

  private boolean handleUnsubscribeMsg(WxMessage message) {
    // TODO Auto-generated method stub
    return true;
  }

  private boolean handleClickMsg(WxMessage message) {
    if ("com.yikeshangshou.wx.share.plan".equals(message.getEventKey())) {

    }
    if ("com.yikeshangshou.wx.free".equals(message.getEventKey())) {
      return handleFreeClickEvent(message);
    }

    return true;
  }

  private boolean handleScanMsg(WxMessage message) {
    if (isInvitationEvent(message)) {
      return handleInvitationEvent(message);
    }
    return true;
  }

  private boolean isInvitationEvent(WxMessage message) {
    boolean is = false;
    String eventKey = message.getEventKey();
    if (StringUtils.isNotEmpty(eventKey)) {
      if (eventKey.contains("qrscene_")) {
        if (eventKey.contains("inv_")) {
          is = true;
        }
      }
    }
    if (!is) {
      String qrTicket = message.getTicket();
      is = entityDao.exists("wx_user", "qrTicket", qrTicket);
    }
    return is;
  }

  private boolean handleInvitationEvent(WxMessage message) {
    boolean useInvitationCode = message.getEventKey().contains("qrscene_") && message.getEventKey().contains("inv_");

    WxUser sourceUser;
    if (useInvitationCode) {
      String invitationCode = message.getEventKey().replace("qrscene_", "");
      sourceUser = findWxUserByInvitationCode(invitationCode);
      if (sourceUser == null) {
        LOG.error("Not found source WxUser with invitation code : " + invitationCode);
        return false;
      }
    } else {
      String ticket = message.getTicket();
      sourceUser  = findWxUserByQrTicket(ticket);
      if (sourceUser == null) {
        LOG.error("Not found source WxUser with qeTicket : " + ticket);
        return false;
      }
    }
    if (StringUtils.equals(sourceUser.getOpenid(), message.getFromUserName())) {
      // 自己不能邀请自己
      return false;
    }
    WxUser scannedUser = WxApiUtils.requestWxUser(message.getFromUserName());
    if (scannedUser == null) {
      return false;
    }
    long scannedUserId = saveWxUser(scannedUser, message.getFromUserName());
    if (0 == scannedUserId) {
      return false;
    }
    if (!saveWxUserInvitationUserId(scannedUserId, sourceUser.getId())) {
      return false;
    }

    int count = entityDao.count("wx_user", "invitationFromWxUserId", sourceUser.getId());
    if (count <= 2) {
      // TODO 发送消息给sourceUser
      WxApiUtils.sendTextMessage("有一位好友接受了你的邀请！", sourceUser.getOpenid());
      WxApiUtils.sendTextMessage("你是第" + String.valueOf(count) + "位支持者！", message.getFromUserName());
      if (count == 2) {
        // TODO 更改sourceUser为已入学
        updateWxUserAsStudent(sourceUser.getId());
        WxApiUtils.sendTextMessage("恭喜你已成功入学！", sourceUser.getOpenid());
      }
    }

    return true;
  }

  private boolean handleFreeClickEvent(final WxMessage message) {

    WxApiUtils.sendTextMessage("滴~  学生卡 (*￣▽￣*) \n\n"
            + "限时名额有限，请在1小时内将下方专属邀请卡发送朋友圈或群哦~ \n\n"
            + "Ps:（完成 2 个朋友扫码支持，系统会自动给您发送入学通知）\n\n"
            + "↓↓邀请卡正在生成中↓↓", message.getFromUserName());
    executor.execute(new Runnable() {
      public void run() {
        WxUser user = WxApiUtils.requestWxUser(message.getFromUserName());
        if (user != null) {
          saveWxUser(user, message.getFromUserName());
          sendInvitationImage(user, message);
        } else {
          WxApiUtils.sendTextMessage("图片生成失败，请稍后再试。", message.getFromUserName());
        }
      }
    });
    return true;
  }

  private boolean sendInvitationImage(WxUser user, WxMessage message) {

    String invitationCode = formateInvitationCode(user.getOpenid());

    String ticket = WxApiUtils.requestQRCode(invitationCode);

    saveWxUserInvitationCode(message.getFromUserName(), invitationCode, ticket);

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

  // TODO finish sendTnvitingTemplateMessage
  private boolean sendTnvitingTemplateMessage(WxUser scanner, WxUser inviter, int targetCount, int finishCount, int remainCount) {
    long time = System.currentTimeMillis();
    String templateId = "foo";
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("subtitle", _formateTemplateData("你已协助联盟完成1为支持者", "#FF2C38"));
    data.put("userName", _formateTemplateData(scanner.getNickname(), "#333333"));
    data.put("submitTime", _formateTemplateData(String.valueOf(time), "#333333"));
    data.put("targetCount", _formateTemplateData(String.valueOf(targetCount), "#333333"));
    data.put("finishCount", _formateTemplateData(String.valueOf(finishCount), "#333333"));
    data.put("remainCount", _formateTemplateData(String.valueOf(remainCount), "#333333"));
    return WxApiUtils.sendTemplateMessage(templateId, null, data, inviter.getOpenid());
  }

  // TODO fooooo
  private Map<String, Object> _formateTemplateData(String value, String color) {
    Map<String, Object> foo = new HashMap<String, Object>();
    foo.put("value", value);
    foo.put("color", color);
    return foo;
  }

  private String formateInvitationCode(String openId) {
    if (StringUtils.isEmpty(openId)) {
      return "";
    }
    openId = "inv_" + openId;
    return openId.substring(0, 64);
  }

  private boolean saveWxUserInvitationCode(String openId, String code, String ticket) {
    Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
    wxUserFindCondition.put("openId", openId);
    WxUser existUser = entityDao.findOne("wx_user", wxUserFindCondition, WxUserRowMapper.getInstance());
    if (existUser == null) {
      return false;
    }
    if (StringUtils.isNotEmpty(existUser.getInvitationCode())) {
      return true;
    }
    Map<String, Object> updateValues = new HashMap<String, Object>();
    updateValues.put("invitationCode", code);
    updateValues.put("qrTicket", ticket);
    try {
      entityDao.update("wx_user", wxUserFindCondition, updateValues);
      return true;
    } catch (Throwable t) {
      LOG.error("save invitation code failure", t);
    }
    return false;
  }

  private WxUser findWxUserByOpenId(String openId) {
    Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
    wxUserFindCondition.put("openId", openId);
    return entityDao.findOne("wx_user", wxUserFindCondition, WxUserRowMapper.getInstance());
  }

  private WxUser findWxUserByQrTicket(String ticket) {
    Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
    wxUserFindCondition.put("qrTicket", ticket);
    return entityDao.findOne("wx_user", wxUserFindCondition, WxUserRowMapper.getInstance());
  }

  private WxUser findWxUserByInvitationCode(String code) {
    Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
    wxUserFindCondition.put("invitationCode", code);
    return entityDao.findOne("wx_user", wxUserFindCondition, WxUserRowMapper.getInstance());
  }

  private boolean updateWxUserAsStudent(long id) {
    try {
      entityDao.update("wx_user", "id", id, "isStudent", 1);
      return true;
    } catch (Throwable t) {
      LOG.error("update WxUser as a student failure", t);
    }
    return false;
  }

  /**
   * @param scannedUserId 扫描者的id
   * @param inviterUserId 发送邀请者的id
   * @return 是否保存
   */
  private boolean saveWxUserInvitationUserId(long scannedUserId, long inviterUserId) {
    try {
      entityDao.update("wx_user", "id", scannedUserId, "invitationFromWxUserId", inviterUserId);
      return true;
    } catch (Throwable t) {
      LOG.error("save WxUser inviter failure", t);
    }
    return false;
  }

  private long saveWxUser(WxUser user, String openId) {
    Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
    wxUserFindCondition.put("openId", openId);
    WxUser existUser = entityDao.findOne("wx_user", wxUserFindCondition, WxUserRowMapper.getInstance());
    if (existUser != null) {
      if (!StringUtils.equals(existUser.getHeadimgurl(), user.getHeadimgurl()) && StringUtils.isNotEmpty(user.getHeadimgurl())) {
        try {
          entityDao.update("wx_user", wxUserFindCondition, "headImgUrl", user.getHeadimgurl());
        } catch (Throwable t) {
          LOG.error("update WxUser headImgUrl failure", t);
        }
      }
      return existUser.getId();
    }
    long createTime = System.currentTimeMillis();
    Entity entity = new Entity("wx_user");
    entity.set("createTime", createTime);
    entity.set("subscribe", user.getSubscribe());
    entity.set("openid", user.getOpenid());
    entity.set("isStudent", 0);
    if (0 != user.getSubscribe()) {
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
      entity = entityDao.saveAndReturn(entity);
      return entity.getId();
    } catch (Throwable t) {
      LOG.error("save WxUser failure", t);
      return 0;
    }
  }


}
