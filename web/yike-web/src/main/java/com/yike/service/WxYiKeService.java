package com.yike.service;

import com.yike.model.WxMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * @author ilakeyc
 * @since 2017/2/17
 */
@Service
public class WxYiKeService {
  private static final Log LOG = LogFactory.getLog(WxYiKeService.class);

  public static String WX_TOKEN = "yikeshangshouwx";
  public static String APP_ID = "";
  public static String APP_SECRET = "";

  public boolean handleMessage(WxMessage message) {
    return true;
  }
}
