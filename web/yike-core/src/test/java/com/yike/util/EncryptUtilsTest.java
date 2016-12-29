package com.yike.util;

import org.junit.Test;

import com.yike.util.EncryptUtils;

/**
 * @author xueqiangmi
 * @since May 6, 2013
 */
public final class EncryptUtilsTest {

  @Test
  public void testEncrypt() {
    System.out.println(EncryptUtils.encryptPlainPassword("Tb034780"));
  }

}
