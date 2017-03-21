package com.yike.task;

import com.yike.Constants;
import com.yike.dao.EntityDao;
import com.yike.dao.mapper.WxTextResponseRowMapper;
import com.yike.model.WxButton;
import com.yike.model.WxTextResponse;
import com.yike.service.WxITService;
import com.yike.service.WxYiKeService;
import com.yike.web.util.WxTextResponseUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 08/02/2017
 */
@Service
public class WxServiceScheduler implements Runnable {
    private static final Log LOG = LogFactory.getLog(WxServiceScheduler.class);

    @Resource
    protected EntityDao entityDao;

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
        setupWxResponses();
    }

    public void setupWxResponses() {

        List<WxTextResponse> yike = entityDao
                .find("wx_yike_text_response",
                        "status",
                        Constants.STATUS_OK,
                        WxTextResponseRowMapper.getInstance());
        List<WxTextResponse> it = entityDao
                .find("wx_text_response",
                        "status",
                        Constants.STATUS_OK,
                        WxTextResponseRowMapper.getInstance());

        WxTextResponseUtils.getYikeResponses().clear();
        for (WxTextResponse response : yike) {
            WxTextResponseUtils.getYikeResponses()
                    .put(response.getTarget(), response.getResult());
        }

        WxTextResponseUtils.getItResponses().clear();
        for (WxTextResponse response : it) {
            WxTextResponseUtils.getItResponses()
                    .put(response.getTarget(), response.getResult());
        }
    }

    private void setYiKeButtons() {

        WxButton mine = new WxButton("我");
        mine.addSubButton(new WxButton(
                "帐号绑定",
                WxButton.typeClick,
                "com.yikeshangshou.wx.binding",
                null));
        mine.addSubButton(new WxButton(
                "我报名的",
                WxButton.typeClick,
                "com.yikeshangshou.wx.application",
                null));

        WxButton course = new WxButton("课程");
        course.addSubButton(new WxButton(
                "计算机技术",
                WxButton.typeView,
                null,
                "http://www.yikeshangshou.com/course?c=102"));
        course.addSubButton(new WxButton(
                "语言翻译",
                WxButton.typeView,
                null,
                "http://www.yikeshangshou.com/course?c=101"));
        course.addSubButton(new WxButton(
                "生活技能",
                WxButton.typeView,
                null,
                "http://www.yikeshangshou.com/course?c=103"));

        WxButton about = new WxButton(
                "资源下载",
                WxButton.typeClick,
                "com.yikeshangshou.wx.download",
                null);

        List<WxButton> buttons = new ArrayList<WxButton>();
        buttons.add(mine);
        buttons.add(course);
        buttons.add(about);

        Map<String, Object> main = new HashMap<String, Object>();
        main.put("button", buttons);

        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + WxYiKeService.apiUtils.currentAccessToken;
        WxYiKeService.apiUtils.postJsonToObject(url, main, null);
    }

    private void setITButtons() {

        WxButton warmUp = new WxButton("实战课程");
        warmUp.addSubButton(new WxButton(
                "周六公开课",
                WxButton.typeView,
                null,
                "http://mp.weixin.qq.com/s/a1UkpFAb2z7Lz5LAOwy5rg"));
        warmUp.addSubButton(new WxButton(
                "当前课程",
                WxButton.typeView,
                null,
                "http://mp.weixin.qq.com/s/HQoQmU6wGDEyJcgkKo6teg"));
        warmUp.addSubButton(new WxButton(
                "git教程",
                WxButton.typeView,
                null,
                "http://mp.weixin.qq.com/s/M4qztb2JfDwsrdjdrmgKEg"));
        warmUp.addSubButton(new WxButton(
                "当前课程源码",
                WxButton.typeClick,
                "com.yikeshangshou.wx.source",
                null));

        WxButton free = new WxButton(
                "免费入学",
                WxButton.typeClick,
                "com.yikeshangshou.wx.free",
                null);

        WxButton share = new WxButton("资源共享");
        share.addSubButton(new WxButton(
                "资源共享计划",
                WxButton.typeView,
                null,
                "http://mp.weixin.qq.com/s/NlThXqnfFQ7_d8MKF6wA4w"));
        share.addSubButton(new WxButton(
                "前端学习资源",
                WxButton.typeClick,
                "com.yikeshangshou.wx.share.web",
                null));
        share.addSubButton(new WxButton(
                "Java学习资源",
                WxButton.typeClick,
                "com.yikeshangshou.wx.share.java",
                null));
        share.addSubButton(new WxButton(
                "数据学习资源",
                WxButton.typeClick,
                "com.yikeshangshou.wx.share.db",
                null));

        List<WxButton> buttons = new ArrayList<WxButton>();
        buttons.add(warmUp);
        buttons.add(free);
        buttons.add(share);

        Map<String, Object> main = new HashMap<String, Object>();
        main.put("button", buttons);

        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + WxITService.apiUtils.currentAccessToken;
        WxITService.apiUtils.postJsonToObject(url, main, null);

    }
}
