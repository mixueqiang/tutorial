package com.yike.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yike.model.WxMessage;
import com.yike.web.util.SimpleNetworking;

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


  public static boolean sendTextMessage(String content, String toUser, boolean needCheckAccessToken) {

    String messageSendUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + WX_ACCESS_TOKEN;

    Map<String, String> message = new HashMap<String, String>();
    message.put("content", content);

    Map<String, Object> foo = new HashMap<String, Object>();
    foo.put("touser", toUser);
    foo.put("msgtype", "text");
    foo.put("text", message);

    Gson g = new Gson();

    String postJson = g.toJson(foo);
    LOG.info("postJson :" + postJson); 
	String postResult = SimpleNetworking.postRequest(messageSendUrl, postJson);

    Map<String, String> result = g.fromJson(postResult, new TypeToken<Map<String, String>>() {
    }.getType());

    String errCode = result.get("errcode");

    if ("40014".equals(errCode) && needCheckAccessToken) {
      return requestAccessToken() && sendTextMessage(content, toUser, false);
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
		WxService.sendTextMessage("滴~  学生卡 (*￣▽￣*) \n\n"
				+ "限时名额有限，请在1小时内将下方专属邀请卡发送朋友圈或群哦~ \n\n"
				+ "Ps:（完成 2 个朋友扫码支持，系统会自动给您发送入学通知）\n\n"
				+ "↓↓邀请卡正在生成中↓↓", wxMessage.getFromUserName(), true);
		//TODO 异步生成一张图片，发送一张图片
		WxService.sendTextMessage("假装我是一张图片", wxMessage.getFromUserName(), true);
	}
	
	return true;
}

private static boolean handleTextMsg(WxMessage wxMessage) {
	WxService.sendTextMessage("消息已收到，暂无关于" + wxMessage.getContent() + "的回复", wxMessage.getFromUserName(), true);
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

}
