package com.yike.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yike.Constants;
import com.yike.dao.EntityDao;
import com.yike.model.Entity;
import com.yike.model.WxMessage;
import com.yike.model.WxUser;
import com.yike.web.util.SimpleNetworking;
import com.yike.web.util.WxFotoMixUtil;
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

  public static String WX_TOKEN = "yikeshangshouwx";
  public static String WX_APP_ID = "wxce4aa0af6d3ec704";
  public static String WX_APP_SECRET = "5f8238027cab1b5348df2dd86f5bd6fe";
  public static String WX_ACCESS_TOKEN;

  private static ExecutorService executor = Executors.newFixedThreadPool(10);

  @Resource
  protected EntityDao entityDao;

  @Resource
  protected WxFotoMixUtil wxFotoMixUtil;

  public boolean sendTextMessage(String text, String toUser) {

    Map<String, Object> messageContent = new HashMap<String, Object>();
    messageContent.put("content", text);
    return sendMessage("text", messageContent, toUser, true);

  }

  public boolean sendImageMessage(String mediaId, String toUser) {
    Map<String, Object> imageContent = new HashMap<String, Object>();
    imageContent.put("media_id", mediaId);
    return sendMessage("image", imageContent, toUser, true);
  }


  public boolean sendMessage(String type, Map<String, Object> content, String toUser, boolean needCheckAccessToken) {

    String messageSendUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + WX_ACCESS_TOKEN;

    Map<String, Object> foo = new HashMap<String, Object>();
    foo.put("touser", toUser);
    foo.put("msgtype", type);
    foo.put(type, content);

    Gson g = new Gson();

    String postJson = g.toJson(foo);
    LOG.info("postJson :" + postJson);
    String postResult = SimpleNetworking.postRequest(messageSendUrl, postJson);

    Map<String, String> result = g.fromJson(postResult, new TypeToken<Map<String, String>>() {
    }.getType());

    String errCode = result.get("errcode");

    if ("40014".equals(errCode) && needCheckAccessToken) {
      return requestAccessToken() && sendMessage(type, content, toUser, false);
    }
    return "0".equals(errCode);
  }

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

    final WxUser user = requestWxUser(message.getFromUserName(), true);
    if (user == null) {
      return false;
    }
    sendTextMessage("滴~  学生卡 (*￣▽￣*) \n\n"
            + "限时名额有限，请在1小时内将下方专属邀请卡发送朋友圈或群哦~ \n\n"
            + "Ps:（完成 2 个朋友扫码支持，系统会自动给您发送入学通知）\n\n"
            + "↓↓邀请卡正在生成中↓↓", message.getFromUserName());
    executor.execute(new Runnable() {
      public void run() {
        if (!sendInvitationImage(user, message)) {
          sendTextMessage("图片生成失败，请稍后再试。", message.getFromUserName());
        }
      }
    });
    return true;
  }

  private boolean sendInvitationImage(WxUser user, WxMessage message) {
    File image = wxFotoMixUtil.createInvitationImage(user);
    if (image == null) {
      sendTextMessage("图片生成失败，请稍后再试。", message.getFromUserName());
      return false;
    }
    String imageUploadURL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=" + WX_ACCESS_TOKEN + "&type=image";
    String responseString = SimpleNetworking.uploadImage(imageUploadURL, image);
    Gson g = new Gson();
    try {
      Map<String, String> response = g.fromJson(responseString, new TypeToken<Map<String, String>>() {
      }.getType());
      if (response != null) {
        String id = response.get("media_id");
        return sendImageMessage(id, message.getFromUserName());
      }
    } catch (Throwable t) {
      LOG.error("json解析失败", t);
    }
    return false;
  }


  private boolean handleTextMsg(WxMessage wxMessage) {
    sendTextMessage("消息已收到，暂无关于" + wxMessage.getContent() + "的回复", wxMessage.getFromUserName());
    return true;
  }

  public boolean requestAccessToken() {

    String urlString = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WX_APP_ID + "&secret=" + WX_APP_SECRET;

    String result = SimpleNetworking.getRequest(urlString);

    Gson gson = new Gson();

    Map<String, Object> response = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
    }.getType());

    String token = (String) response.get("access_token");

    WX_ACCESS_TOKEN = token;

    return !(StringUtils.isEmpty(token) || "40013".equals(response.get("errcode")));
  }

  public WxUser requestWxUser(String openId, boolean needCheckAccessToken) {
    String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + WX_ACCESS_TOKEN + "&openid=" + openId + "&lang=zh_CN";
    String responseString = SimpleNetworking.getRequest(url);
    System.out.println(responseString);
    Gson g = new Gson();
    WxUser user;
    try {
      user = g.fromJson(responseString, WxUser.class);
      if (user != null) {
        saveUser(user, openId);
      }
      return user;
    } catch (JsonSyntaxException j) {
      LOG.error("WxUser user = g.fromJson(responseString, WxUser.class);", j);
    }

    return null;
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
      entity.set("subscribeTime", user.getSubscribe_time());
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

  public String requestQRCode(String sceneString) {
    String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + WX_ACCESS_TOKEN;

    Map<String, Object> main = new HashMap<String, Object>();
    Map<String, Object> actionInfo = new HashMap<String, Object>();
    Map<String, Object> scene = new HashMap<String, Object>();

    scene.put("scene_str", sceneString);
    actionInfo.put("scene", scene);
    main.put("action_name", "QR_LIMIT_STR_SCENE");
    main.put("action_info", actionInfo);

    Gson g = new Gson();

    String postString = g.toJson(main);

    String responseString = SimpleNetworking.postRequest(url, postString);

    try {
      Map<String, String> response = g.fromJson(responseString, new TypeToken<Map<String, String>>() {
      }.getType());
      return response.get("ticket");
    } catch (Throwable t) {
      t.printStackTrace();
      return "";
    }
  }

}
