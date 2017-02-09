package com.yike.service;

import com.google.gson.Gson;
import com.yike.dao.EntityDao;
import com.yike.model.WxAccessToken;
import com.yike.task.TaskScheduler;
import com.yike.web.util.SimpleNetworking;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 08/02/2017
 */
@Service
public class WxService implements Runnable {
  private static final Log LOG = LogFactory.getLog(WxService.class);

  public static String WX_TOKEN = "yikeshangshouwx";
  public static String WX_APP_ID = "wxce4aa0af6d3ec704";
  public static String WX_APP_SECRET = "5f8238027cab1b5348df2dd86f5bd6fe";
  public static String WX_ACCESS_TOKEN;


  @Resource
  protected EntityDao entityDao;

  public WxService() {

    TaskScheduler.register(getClass().getSimpleName(), this, 2, 7100);
  }


  @Override
  public void run() {
    requestAccessToken();
    setButtons();
  }


  public void requestAccessToken() {

    String urlString = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WX_APP_ID + "&secret=" + WX_APP_SECRET;

    String result = SimpleNetworking.getRequest(urlString);

    Gson gson = new Gson();

    WxAccessToken token = gson.fromJson(result, WxAccessToken.class);

    WX_ACCESS_TOKEN = token.getAccess_token();
  }


  private void setButtons() {
    if (WX_ACCESS_TOKEN == null) {
      requestAccessToken();
    }
    Map<String, Object> parameter = new HashMap<String, Object>();

    Map<String, Object> button_free = new HashMap<String, Object>();
    button_free.put("type", "click");
    button_free.put("name", "免费入学");
    button_free.put("key", "com.yikeshangshou.wx.free");

    Map<String, Object> button_share = new HashMap<String, Object>();
    button_share.put("name", "资源共享");

    ArrayList<Map<String, Object>> button_share_sub = new ArrayList<Map<String, Object>>();
    Map<String, Object> button_share_plan = new HashMap<String, Object>();
    button_share_plan.put("type", "click");
    button_share_plan.put("name", "资源共享计划");
    button_share_plan.put("key", "com.yikeshangshou.wx.share.plan");

    Map<String, Object> button_share_web = new HashMap<String, Object>();
    button_share_web.put("type", "click");
    button_share_web.put("name", "前端学习资源");
    button_share_web.put("key", "com.yikeshangshou.wx.share.web");

    Map<String, Object> button_share_java = new HashMap<String, Object>();
    button_share_java.put("type", "click");
    button_share_java.put("name", "Java学习资源");
    button_share_java.put("key", "com.yikeshangshou.wx.share.java");

    Map<String, Object> button_share_db = new HashMap<String, Object>();
    button_share_db.put("type", "click");
    button_share_db.put("name", "数据学习资源");
    button_share_db.put("key", "com.yikeshangshou.wx.share.db");

    button_share_sub.add(button_share_db);
    button_share_sub.add(button_share_java);
    button_share_sub.add(button_share_web);
    button_share_sub.add(button_share_plan);
    button_share.put("sub_button", button_share_sub);

    ArrayList<Map<String, Object>> buttons = new ArrayList<Map<String, Object>>();
    buttons.add(button_free);
    buttons.add(button_share);

    parameter.put("button", buttons);

    Gson gson = new Gson();
    String result = SimpleNetworking.postRequest(" https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + WX_ACCESS_TOKEN, gson.toJson(parameter));
    System.out.print(result);
  }

}
