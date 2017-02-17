package com.yike.web.util;

import com.google.gson.reflect.TypeToken;
import com.yike.service.WxITService;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/2/15
 */
public class WxApiUtilsTest {

  @Test
  public void mainTest() throws Exception {
    WxITService.apiUtils.requestAccessToken();

    getTemplateList();

    postTemplate();
  }


  private void getTemplateList() {
    Map<String, Object> result = WxITService.apiUtils.getJsonToObject("https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=" + WxITService.apiUtils.currentAccessToken, new TypeToken<Map<String, Object>>() {
    }.getType());

    System.out.println(result);
  }

  private void postTemplate() {
//    ARRmIiiyvlE9E78JIaKoBiW3ATtaESofyoCcscXW208
    Map<String, Object> main = new HashMap<String, Object>();
    main.put("touser", "oCooQw1i05m1hcThyqCcoIpVGzaU");
    main.put("template_id", "ARRmIiiyvlE9E78JIaKoBiW3ATtaESofyoCcscXW208");


    Map<String, Object> content = new HashMap<String, Object>();
    content.put("value", "hhhhh");
    content.put("color", "#333333");

    Map<String, Object> data = new HashMap<String, Object>();
    data.put("content", content);
    main.put("data", data);

    Map<String, Object> result = WxITService.apiUtils.postJsonToObject("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + WxITService.apiUtils.currentAccessToken, main, new TypeToken<Map<String, Object>>() {
    }.getType());
    System.out.println(result);
  }

}