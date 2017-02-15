package com.yike.web.util;

import com.google.gson.reflect.TypeToken;
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
    WxApiUtils.requestAccessToken();

    addTemplate("TEMP0000");

    getTemplateList();
  }


  private void addTemplate(String id_short) {

    Map<String, String> foo = new HashMap<String, String>();
    foo.put("template_id_short", id_short);
    Map<String, Object> result = WxApiUtils.postJsonToObject("https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=" + WxApiUtils.WX_ACCESS_TOKEN, foo, new TypeToken<Map<String, Object>>() {
    }.getType());

    System.out.println(result);
  }

  private void getTemplateList() {
    Map<String, Object> result = WxApiUtils.getJsonToObject("https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=" + WxApiUtils.WX_ACCESS_TOKEN, new TypeToken<Map<String, Object>>() {
    }.getType());

    System.out.println(result);
  }

}