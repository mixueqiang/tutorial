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



  public static Map<String, Object> formateNewMemberNotice(String memberId) {

    Map<String, Object> data = new HashMap<String, Object>();
    Format dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
    String dateString = dateFormat.format(new Date());

    data.put("first", formateData("你已协助学院完成1为支持者", "#FF2C38"));
    data.put("keyword1", formateData(memberId, null));
    data.put("keyword2", formateData(dateString, null));

    String remarkString = "任务目标：2人\n" +
            "已经完成：1人\n" +
            "还需完成：1人\n" +
            "棒棒哒~继续努力，马上就可以加入「IT技术成长联盟」啦！";
    data.put("remark", formateData(remarkString, null));
    return data;

  }

  public static Map<String, Object> formateTaskCompletionNotice() {
    Map<String, Object> data = new HashMap<String, Object>();
    Format dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
    String dateString = dateFormat.format(new Date());

    data.put("first", formateData("恭喜你获得「IT技术成长联盟」入学名额！", "#FF2C38"));
    data.put("keyword1", formateData("IT技术成长联盟", null));
    data.put("keyword2", formateData("入学仪式", null));
    data.put("keyword3", formateData(dateString, null));

    String remarkString = "点击↓↓详情，填写入学资料。么么哒~";
    data.put("remark", formateData(remarkString, null));
    return data;
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
