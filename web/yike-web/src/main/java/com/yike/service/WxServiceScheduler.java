package com.yike.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.yike.task.TaskScheduler;


/**
 * @author ilakeyc
 * @since 08/02/2017
 */
@Service
public class WxServiceScheduler implements Runnable {
  private static final Log LOG = LogFactory.getLog(WxServiceScheduler.class);

  @Resource
  protected WxITService wxITService;

  public WxServiceScheduler() {
    TaskScheduler.register(getClass().getSimpleName(),
            this,
            2,
            7100);
  }


  @Override
  public void run() {
    WxITService.apiUtils.requestAccessToken();
    setButtons();
  }


  private void setButtons() {
    Map<String, Object> main = new HashMap<String, Object>();
    
    Map<String, Object> button_warm_up = new HashMap<String, Object>();
    button_warm_up.put("name", "实战课程");
    
    ArrayList<Map<String, Object>> button_warm_up_sub = new ArrayList<Map<String, Object>>();
    Map<String, Object> button_warm_up_course = new HashMap<String, Object>();
    button_warm_up_course.put("type", "view");
    button_warm_up_course.put("name", "当前课程");
    button_warm_up_course.put("url", "http://mp.weixin.qq.com/s/NlThXqnfFQ7_d8MKF6wA4w");
    
    Map<String, Object> button_warm_up_git = new HashMap<String, Object>();
    button_warm_up_git.put("type", "view");
    button_warm_up_git.put("name", "git教程");
    button_warm_up_git.put("url", "http://mp.weixin.qq.com/s/NlThXqnfFQ7_d8MKF6wA4w");
    
    Map<String, Object> button_warm_up_source = new HashMap<String, Object>();
    button_warm_up_source.put("type", "click");
    button_warm_up_source.put("name", "课程源码");
    button_warm_up_source.put("key", "com.yikeshangshou.wx.source");

    
    button_warm_up_sub.add(button_warm_up_course);
    button_warm_up_sub.add(button_warm_up_git);
    button_warm_up_sub.add(button_warm_up_source);
    
    button_warm_up.put("sub_button", button_warm_up_sub);
    
    
    
    Map<String, Object> button_free = new HashMap<String, Object>();
    button_free.put("type", "click");
    button_free.put("name", "免费入学");
    button_free.put("key", "com.yikeshangshou.wx.free");

    Map<String, Object> button_share = new HashMap<String, Object>();
    button_share.put("name", "资源共享");

    ArrayList<Map<String, Object>> button_share_sub = new ArrayList<Map<String, Object>>();
    Map<String, Object> button_share_plan = new HashMap<String, Object>();
    button_share_plan.put("type", "view");
    button_share_plan.put("name", "资源共享计划");
    button_share_plan.put("url", "http://mp.weixin.qq.com/s/NlThXqnfFQ7_d8MKF6wA4w");

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
//    button_share_db.put("url", "https://pan.baidu.com/s/1o7SXZgY");

    button_share_sub.add(button_share_db);
    button_share_sub.add(button_share_java);
    button_share_sub.add(button_share_web);
    button_share_sub.add(button_share_plan);
    button_share.put("sub_button", button_share_sub);

    ArrayList<Map<String, Object>> buttons = new ArrayList<Map<String, Object>>();
    buttons.add(button_warm_up);
    buttons.add(button_free);
    buttons.add(button_share);

    main.put("button", buttons);

    String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + WxITService.apiUtils.currentAccessToken;
    WxITService.apiUtils.postJsonToObject(url, main, null);
  }

}
