package com.yike.util;

import org.junit.Test;

import com.yike.util.SmsUtilsYuntongxun;

/**
 * @author mixueqiang
 * @since Jul 30, 2016
 */
public class SmsUtilsYuntongxunTest {

  @Test
  public void testSend() throws Throwable {
    SmsUtilsYuntongxun.send("18668090654", 1, new String[] { "123456", "1分钟" });
  }

}
