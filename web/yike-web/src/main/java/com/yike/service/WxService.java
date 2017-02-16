package com.yike.service;

import com.yike.Constants;
import com.yike.dao.EntityDao;
import com.yike.dao.mapper.WxUserRowMapper;
import com.yike.model.Entity;
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
public class WxService {
  private static final Log LOG = LogFactory.getLog(WxService.class);

  private static ExecutorService executor = Executors.newFixedThreadPool(10);

  private static String NEW_MEMBER_JOINING_NOTICE_TEMPLATE_ID = "-nKMTybLcXHNsle6Ib5mdpO4ec2xFpCW6zjMX6mPzNQ";
  private static String TASK_COMPLETION_NOTICE_TEMPLATE_ID = "G66-ZH08CQdAZVKWZH3do_pqwqaTDk8AKx_9QNzm1hg";

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

    WxUser user = WxApiUtils.requestWxUser(message.getFromUserName());
    saveWxUser(user, message.getFromUserName());

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
    String qrTicket = message.getTicket();
    return entityDao.exists("wx_user", "qrTicket", qrTicket);
  }

  private boolean handleInvitationEvent(WxMessage message) {

    WxUser sourceUser;
    String ticket = message.getTicket();
    sourceUser = findWxUserByQrTicket(ticket);
    if (sourceUser == null) {
      LOG.error("Not found source WxUser with qeTicket : " + ticket);
      return false;
    }

    if (StringUtils.equals(sourceUser.getOpenid(), message.getFromUserName())) {
      // 自己不能邀请自己
      return false;
    }
    WxUser scannedUser = findWxUserByOpenId(message.getFromUserName());
    if (scannedUser != null) {
      if (0 != scannedUser.getInviterId()) {
        return false;
      }
    } else {
      scannedUser = WxApiUtils.requestWxUser(message.getFromUserName());
    }
    if (scannedUser == null) {
      return false;
    }
    long scannedUserId = saveWxUser(scannedUser, message.getFromUserName());
    if (0 == scannedUserId) {
      LOG.error("Not found scannedUser in database or save failure.");
      return false;
    }
    if (!saveWxUserInvitationUserId(scannedUserId, sourceUser.getId())) {
      return false;
    }
    if (sourceUser.getIsStudent() == 1) {
      return true;
    }
    int count = entityDao.count("wx_user", "inviterId", sourceUser.getId());
    if (count == 1) {
      return sendInvitingTemplateMessage(scannedUser, sourceUser);
    }
    if (count == 2) {
      if (updateWxUserAsStudent(sourceUser.getId())) {
        return sendFreeAdmissionTemplateMessage(sourceUser);
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
        WxUser user = findWxUserByOpenId(message.getFromUserName());
        if (user != null) {
          sendInvitationImage(user, message);
        } else {
          WxApiUtils.sendTextMessage("图片生成失败，请稍后再试。", message.getFromUserName());
        }
      }
    });
    return true;
  }

  private boolean sendInvitationImage(WxUser user, WxMessage message) {
    File image = null;
    String ticket = user.getQrTicket();
    if (StringUtils.isNotEmpty(ticket)) {
      image = WxFotoMixUtils.localInvitationImage(user.getQrTicket());
    } else {
      String invitationCode = formateInvitationCode(user.getOpenid());
      ticket = WxApiUtils.requestQRCode(invitationCode);
    }
    if (image == null) {
      image = WxFotoMixUtils.createInvitationImage(user, ticket);
    }

    if (image == null) {
      WxApiUtils.sendTextMessage("图片生成失败，请稍后再试。", message.getFromUserName());
      return false;
    }

    saveWxUserQrTicket(message.getFromUserName(), ticket);
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


  private boolean sendInvitingTemplateMessage(WxUser scanner, WxUser inviter) {
    Map<String, Object> data = WxTemplateMessageFormatter.formateNewMemberNotice(scanner.getNickname());
    return WxApiUtils.sendTemplateMessage(NEW_MEMBER_JOINING_NOTICE_TEMPLATE_ID, null, data, inviter.getOpenid());
  }

  private boolean sendFreeAdmissionTemplateMessage(WxUser toUser) {
    Map<String, Object> data = WxTemplateMessageFormatter.formateTaskCompletionNotice();
    return WxApiUtils.sendTemplateMessage(TASK_COMPLETION_NOTICE_TEMPLATE_ID, "https://www.sojump.hk/jq/12131778.aspx", data, toUser.getOpenid());
  }


  private String formateInvitationCode(String openId) {
    if (StringUtils.isEmpty(openId)) {
      return "";
    }
    openId = "inv_" + openId;
    if (openId.length() > 64) {
      openId = openId.substring(0, 64);
    }
    return openId;
  }

  private boolean saveWxUserQrTicket(String openId, String ticket) {
    Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
    wxUserFindCondition.put("openId", openId);
    WxUser existUser = entityDao.findOne("wx_user", wxUserFindCondition, WxUserRowMapper.getInstance());
    if (existUser == null) {
      return false;
    }
    Map<String, Object> updateValues = new HashMap<String, Object>();
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
      entityDao.update("wx_user", "id", scannedUserId, "inviterId", inviterUserId);
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
