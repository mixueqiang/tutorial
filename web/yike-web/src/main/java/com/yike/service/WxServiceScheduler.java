package com.yike.service;

import com.yike.task.TaskScheduler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 08/02/2017
 */
@Service
public class WxServiceScheduler implements Runnable {
    private static final Log LOG = LogFactory.getLog(WxServiceScheduler.class);

    public WxServiceScheduler() {
        TaskScheduler.register(getClass().getSimpleName(),
                this,
                2,
                7100);
    }

    @Override
    public void run() {
        WxITService.apiUtils.requestAccessToken();
        WxYiKeService.apiUtils.requestAccessToken();
        setITButtons();
        setYiKeButtons();
    }

    private void setYiKeButtons() {

        ArrayList<Map<String, Object>> button_mine_sub = new ArrayList<Map<String, Object>>();
        button_mine_sub.add(setupButton(
                "click",
                "帐号绑定",
                "com.yikeshangshou.wx.binding",
                null,
                null));
        button_mine_sub.add(setupButton(
                "click",
                "我报名的",
                "com.yikeshangshou.wx.application",
                null,
                null));

        Map<String, Object> button_mine = setupButton(
                null,
                "我",
                null,
                null,
                button_mine_sub);

        ArrayList<Map<String, Object>> button_course_sub = new ArrayList<Map<String, Object>>();
        button_course_sub.add(setupButton(
                "view",
                "计算机技术",
                null,
                "http://www.yikeshangshou.com/course?c=102",
                null));
        button_course_sub.add(setupButton(
                "view",
                "语言翻译",
                null,
                "http://www.yikeshangshou.com/course?c=101",
                null));
        button_course_sub.add(setupButton(
                "view",
                "生活技能",
                null,
                "http://www.yikeshangshou.com/course?c=103",
                null));

        Map<String, Object> button_course = setupButton(
                null,
                "课程",
                null,
                null,
                button_course_sub);

        Map<String, Object> button_about = setupButton(
                "click",
                "关于我们",
                "com.yikeshangshou.wx.about",
                null,
                null);

        ArrayList<Map<String, Object>> buttons = new ArrayList<Map<String, Object>>();
        buttons.add(button_mine);
        buttons.add(button_course);
        buttons.add(button_about);

        Map<String, Object> main = new HashMap<String, Object>();
        main.put("button", buttons);

        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + WxYiKeService.apiUtils.currentAccessToken;
        WxYiKeService.apiUtils.postJsonToObject(url, main, null);
    }

    private void setITButtons() {

        ArrayList<Map<String, Object>> button_warm_up_sub = new ArrayList<Map<String, Object>>();
        button_warm_up_sub.add(setupButton(
                "view",
                "当前课程",
                null,
                "http://mp.weixin.qq.com/s/NlThXqnfFQ7_d8MKF6wA4w",
                null));

        button_warm_up_sub.add(setupButton(
                "view",
                "git教程",
                null,
                "http://mp.weixin.qq.com/s/M4qztb2JfDwsrdjdrmgKEg",
                null));

        button_warm_up_sub.add(setupButton(
                "click",
                "当前课程源码",
                "com.yikeshangshou.wx.source",
                null,
                null));
        Map<String, Object> button_warm_up = setupButton(
                null,
                "实战课程",
                null,
                null,
                button_warm_up_sub);

        Map<String, Object> button_free = setupButton(
                "click",
                "免费入学",
                "com.yikeshangshou.wx.free",
                null,
                null);

        ArrayList<Map<String, Object>> button_share_sub = new ArrayList<Map<String, Object>>();
        button_share_sub.add(setupButton(
                "view",
                "资源共享计划",
                null,
                "http://mp.weixin.qq.com/s/NlThXqnfFQ7_d8MKF6wA4w",
                null));
        button_share_sub.add(setupButton(
                "click",
                "前端学习资源",
                "com.yikeshangshou.wx.share.web",
                null,
                null));
        button_share_sub.add(setupButton(
                "click",
                "Java学习资源",
                "com.yikeshangshou.wx.share.java",
                null,
                null));
        button_share_sub.add(setupButton(
                "click",
                "数据学习资源",
                "com.yikeshangshou.wx.share.db",
                null,
                null));
        Map<String, Object> button_share = setupButton(
                null,
                "资源共享",
                null,
                null,
                button_share_sub);

        ArrayList<Map<String, Object>> buttons = new ArrayList<Map<String, Object>>();
        buttons.add(button_warm_up);
        buttons.add(button_free);
        buttons.add(button_share);

        Map<String, Object> main = new HashMap<String, Object>();
        main.put("button", buttons);

        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + WxYiKeService.apiUtils.currentAccessToken;
        WxITService.apiUtils.postJsonToObject(url, main, null);
    }

//    private void setITButtons() {
//        Map<String, Object> main = new HashMap<String, Object>();
//
//        Map<String, Object> button_warm_up = new HashMap<String, Object>();
//        button_warm_up.put("name", "实战课程");
//
//        ArrayList<Map<String, Object>> button_warm_up_sub = new ArrayList<Map<String, Object>>();
//        Map<String, Object> button_warm_up_course = new HashMap<String, Object>();
//        button_warm_up_course.put("type", "view");
//        button_warm_up_course.put("name", "当前课程");
//        button_warm_up_course.put("url", "http://mp.weixin.qq.com/s/NlThXqnfFQ7_d8MKF6wA4w");
//
//        Map<String, Object> button_warm_up_git = new HashMap<String, Object>();
//        button_warm_up_git.put("type", "view");
//        button_warm_up_git.put("name", "git教程");
//        button_warm_up_git.put("url", "http://mp.weixin.qq.com/s/M4qztb2JfDwsrdjdrmgKEg");
//
//        Map<String, Object> button_warm_up_source = new HashMap<String, Object>();
//        button_warm_up_source.put("type", "click");
//        button_warm_up_source.put("name", "当前课程源码");
//        button_warm_up_source.put("key", "com.yikeshangshou.wx.source");
//
//        button_warm_up_sub.add(button_warm_up_course);
//        button_warm_up_sub.add(button_warm_up_git);
//        button_warm_up_sub.add(button_warm_up_source);
//
//        button_warm_up.put("sub_button", button_warm_up_sub);
//
//        Map<String, Object> button_free = new HashMap<String, Object>();
//        button_free.put("type", "click");
//        button_free.put("name", "免费入学");
//        button_free.put("key", "com.yikeshangshou.wx.free");
//
//        Map<String, Object> button_share = new HashMap<String, Object>();
//        button_share.put("name", "资源共享");
//
//        ArrayList<Map<String, Object>> button_share_sub = new ArrayList<Map<String, Object>>();
//        Map<String, Object> button_share_plan = new HashMap<String, Object>();
//        button_share_plan.put("type", "view");
//        button_share_plan.put("name", "资源共享计划");
//        button_share_plan.put("url", "http://mp.weixin.qq.com/s/NlThXqnfFQ7_d8MKF6wA4w");
//
//        Map<String, Object> button_share_web = new HashMap<String, Object>();
//        button_share_web.put("type", "click");
//        button_share_web.put("name", "前端学习资源");
//        button_share_web.put("key", "com.yikeshangshou.wx.share.web");
//
//        Map<String, Object> button_share_java = new HashMap<String, Object>();
//        button_share_java.put("type", "click");
//        button_share_java.put("name", "Java学习资源");
//        button_share_java.put("key", "com.yikeshangshou.wx.share.java");
//
//        Map<String, Object> button_share_db = new HashMap<String, Object>();
//        button_share_db.put("type", "click");
//        button_share_db.put("name", "数据学习资源");
//        button_share_db.put("key", "com.yikeshangshou.wx.share.db");
////    button_share_db.put("url", "https://pan.baidu.com/s/1o7SXZgY");
//
//        button_share_sub.add(button_share_db);
//        button_share_sub.add(button_share_java);
//        button_share_sub.add(button_share_web);
//        button_share_sub.add(button_share_plan);
//        button_share.put("sub_button", button_share_sub);
//
//        ArrayList<Map<String, Object>> buttons = new ArrayList<Map<String, Object>>();
//        buttons.add(button_warm_up);
//        buttons.add(button_free);
//        buttons.add(button_share);
//
//        main.put("button", buttons);
//
//        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + WxITService.apiUtils.currentAccessToken;
//        WxITService.apiUtils.postJsonToObject(url, main, null);
//    }

    private Map<String, Object> setupButton(String type, String name, String key, String url, ArrayList<Map<String, Object>> subButtons) {
        Map<String, Object> button = new HashMap<String, Object>();
        button.put("name", name);
        if (subButtons != null && !subButtons.isEmpty()) {
            button.put("sub_button", subButtons);
            return button;
        }
        if (StringUtils.isNotEmpty(key)) {
            button.put("key", key);
        }
        if (StringUtils.isNotEmpty(url)) {
            button.put("url", url);
        }
        if (StringUtils.isNotEmpty(type)) {
            button.put("type", type);
        }
        return button;
    }

}
