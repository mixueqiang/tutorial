package com.yike.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
