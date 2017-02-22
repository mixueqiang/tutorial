package com.yike.web.util;

import org.springframework.util.StringUtils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/2/16
 */
public class WxTemplateMessageFormatter {

    public static Map<String, Object> formateBindingSuccessNotice(String phone) {
        Map<String, Object> data = new HashMap<String, Object>();
        Format dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        String dateString = dateFormat.format(new Date());
        String newPhone = phone.substring(7);

        data.put("first", formateData("\n你的帐号已绑定\n", "#FF2C38"));
        data.put("keyword1", formateData("尾号" + newPhone));
        data.put("keyword2", formateData(dateString));
        data.put("remark", formateData("\n若不是您本人操作，请立即重置密码。↓↓"));
        return data;
    }

    public static Map<String, Object> formateHasBindingNotice(String phone, String nickName) {
        Map<String, Object> data = new HashMap<String, Object>();
        Format dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        String dateString = dateFormat.format(new Date());
        String newPhone = phone.substring(7);

        data.put("first", formateData("\n你的帐号已绑定\n", "#FF2C38"));
        data.put("keyword1", formateData("尾号" + newPhone));
        data.put("keyword2", formateData(dateString));
        data.put("remark", formateData(
                "昵称：" + nickName + "\n\n"
                        + "↓↓此帐号可以直接登录【一课上手】"));
        return data;
    }

    public static Map<String, Object> formateBindingPasswordNotice(String phone) {
        Map<String, Object> data = new HashMap<String, Object>();
        Format dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        String dateString = dateFormat.format(new Date());
        String newPhone = phone.substring(7);

        data.put("first", formateData("\n已经绑定尾号为" + newPhone + "的手机号码\n", "#FF2C38"));
        data.put("keyword1", formateData("【昵称】【密码】未设置"));
        data.put("keyword2", formateData(dateString));
        data.put("remark", formateData("\n点击↓↓详情，立即设置密码。"));
        return data;
    }

    public static Map<String, Object> formateUnboundNotice() {
        Map<String, Object> data = new HashMap<String, Object>();
        Format dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        String dateString = dateFormat.format(new Date());

        data.put("first", formateData("\n你还没有绑定【一课上手】帐号\n", "#FF2C38"));
        data.put("keyword1", formateData("未绑定"));
        data.put("keyword2", formateData(dateString));
        data.put("remark", formateData("\n点击↓↓详情，开始绑定帐号。"));
        return data;
    }

    public static Map<String, Object> formateNewMemberNotice(String memberId) {

        Map<String, Object> data = new HashMap<String, Object>();
        Format dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        String dateString = dateFormat.format(new Date());

        data.put("first", formateData("你已协助学院完成1位支持者", "#FF2C38"));
        data.put("keyword1", formateData(memberId));
        data.put("keyword2", formateData(dateString));

        String remarkString = "任务目标：2人\n" +
                "已经完成：1人\n" +
                "还需完成：1人\n" +
                "棒棒哒~还需完成1人就可以免费加入「IT技术成长联盟」啦！";
        data.put("remark", formateData(remarkString));
        return data;

    }

    public static Map<String, Object> formateTaskCompletionNotice() {
        Map<String, Object> data = new HashMap<String, Object>();
        Format dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        String dateString = dateFormat.format(new Date());

        data.put("first", formateData("恭喜你获得「IT技术成长联盟」入学名额！", "#FF2C38"));
        data.put("keyword1", formateData("IT技术成长联盟"));
        data.put("keyword2", formateData("入学仪式"));
        data.put("keyword3", formateData(dateString));

        String remarkString = "点击↓↓详情，填写入学资料。么么哒~";
        data.put("remark", formateData(remarkString));
        return data;
    }

    private static Map<String, Object> formateData(String value) {
        return formateData(value, null);
    }

    private static Map<String, Object> formateData(String value, String color) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("value", value);
        if (StringUtils.isEmpty(color)) {
            color = "#000000";
        }
        data.put("color", color);
        return data;
    }

}
