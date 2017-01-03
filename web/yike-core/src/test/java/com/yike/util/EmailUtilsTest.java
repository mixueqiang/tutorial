package com.yike.util;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Test;

/**
 * @author mixueqiang
 * @since Jan 3, 2017
 *
 */
public class EmailUtilsTest {

  @Test
  public void testSendEmail() throws Exception {
    Email email = new SimpleEmail();
    email.addTo("xueqiang.mi@qq.com");
    email.setFrom("service@yikeshangshou.com", "一课");
    email.setSubject("一课测试邮件");
    email.setMsg("一课测试邮件！");
    EmailUtils.send("no-reply@yikeshangshou.com", email);
  }

}
