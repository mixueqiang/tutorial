package com.yike.service;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonSyntaxException;
import com.yike.model.WxRequestResponse;
import com.yike.model.WxUser;
import com.yike.web.util.WxRequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yike.model.WxMessage;
import com.yike.web.util.SimpleNetworking;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 * @author ilakeyc
 * @since 10/02/2017
 */
public class WxService {

  private static final Log LOG = LogFactory.getLog(WxService.class);

  public static String WX_TOKEN = "yikeshangshouwx";
  public static String WX_APP_ID = "wxce4aa0af6d3ec704";
  public static String WX_APP_SECRET = "5f8238027cab1b5348df2dd86f5bd6fe";
  public static String WX_ACCESS_TOKEN;


  public static boolean sendTextMessage(String text, String toUser) {

    Map<String, Object> messageContent = new HashMap<String, Object>();
    messageContent.put("content", text);
    return sendMessage("text", messageContent, toUser, true);

  }

  public static boolean sendImageMessage(String mediaId, String toUser) {
    Map<String, Object> imageContent = new HashMap<String, Object>();
    imageContent.put("media_id", mediaId);
    return sendMessage("image", imageContent, toUser, true);
  }


  public static boolean sendMessage(String type, Map<String, Object> content, String toUser, boolean needCheckAccessToken) {

    String messageSendUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + WX_ACCESS_TOKEN;

    Map<String, Object> foo = new HashMap<String, Object>();
    foo.put("touser", toUser);
    foo.put("msgtype", type);
    foo.put(type, content);

    Gson g = new Gson();

    String postJson = g.toJson(foo);
    LOG.info("postJson :" + postJson);
    String postResult = SimpleNetworking.postRequest(messageSendUrl, postJson);

    Map<String, String> result = g.fromJson(postResult, new TypeToken<Map<String, String>>() {}.getType());

    String errCode = result.get("errcode");

    if ("40014".equals(errCode) && needCheckAccessToken) {
      return requestAccessToken() && sendMessage(type, content, toUser, false);
    }
    return "0".equals(errCode);
  }

  public static boolean handleMessage(WxMessage wxMessage) {

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

  private static boolean handleEventMsg(WxMessage wxMessage) {
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

  private static boolean handleSubscribeMsg(WxMessage wxMessage) {
    // TODO Auto-generated method stub
    return true;
  }

  private static boolean handleUnsubscribeMsg(WxMessage wxMessage) {
    // TODO Auto-generated method stub
    return true;
  }

  private static boolean handleClickMsg(WxMessage wxMessage) {
    if ("com.yikeshangshou.wx.share.plan".equals(wxMessage.getEventKey())) {

    }
    if ("com.yikeshangshou.wx.free".equals(wxMessage.getEventKey())) {
      WxUser user = requestWxUser(wxMessage.getFromUserName(), true);
      if (null == user) {
        LOG.error("wx: user is null");
      } else {
        LOG.error("wx: " + user.getNickname());
      }
      WxService.sendTextMessage("滴~  学生卡 (*￣▽￣*) \n\n"
              + "限时名额有限，请在1小时内将下方专属邀请卡发送朋友圈或群哦~ \n\n"
              + "Ps:（完成 2 个朋友扫码支持，系统会自动给您发送入学通知）\n\n"
              + "↓↓邀请卡正在生成中↓↓", wxMessage.getFromUserName());
      //TODO 异步生成一张图片，发送一张图片
      if (user != null) {
        if (!StringUtils.isEmpty(user.getNickname())) {
          WxService.sendTextMessage("你好" + user.getNickname(), wxMessage.getFromUserName());
        }
      } else {
        WxService.sendTextMessage("假装我是一张图片", wxMessage.getFromUserName());
      }
    }

    return true;
  }

  private static boolean handleTextMsg(WxMessage wxMessage) {
    WxService.sendTextMessage("消息已收到，暂无关于" + wxMessage.getContent() + "的回复", wxMessage.getFromUserName());
    return true;
  }

  public static boolean requestAccessToken() {

    String urlString = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WX_APP_ID + "&secret=" + WX_APP_SECRET;

    String result = SimpleNetworking.getRequest(urlString);

    Gson gson = new Gson();

    Map<String, Object> response = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
    }.getType());

    String token = (String) response.get("access_token");

    WX_ACCESS_TOKEN = token;

    return !(StringUtils.isEmpty(token) || "40013".equals(response.get("errcode")));
  }

  public static WxUser requestWxUser(String openId, boolean needCheckAccessToken) {
    String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + WX_ACCESS_TOKEN + "&openid="+openId+"&lang=zh_CN";
    String responseString = SimpleNetworking.getRequest(url);
    Gson g = new Gson();
    WxUser user;
    try {
      user = g.fromJson(responseString, WxUser.class);
      return user;
    } catch (JsonSyntaxException j) {
      LOG.error("WxUser user = g.fromJson(responseString, WxUser.class);", j);
    }

    return null;

//    WxRequestResponse response = WxRequestUtils.getJson(url, WxUser.class);
//    if (!StringUtils.isEmpty(response.getErrorCode())) {
//      if ("40014".equals(response.getErrorCode()) && needCheckAccessToken) {
//        if (requestAccessToken()) { return requestWxUser(openId, false); }
//      }
//    }
//    return (WxUser) response.getObject();
  }

}
