package com.yike.util;

import org.junit.Test;

/**
 * @author mixueqiang
 * @since Jan 3, 2017
 *
 */
public class SmsUtilsYunpianTest {

  @Test
  public void testSend() throws Throwable {
    SmsUtilsYunpian.send("18668090654", "register", new String[] { "123456" });
  }

}
