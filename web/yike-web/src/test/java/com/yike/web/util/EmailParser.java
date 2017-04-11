package com.yike.web.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class EmailParser {

  @Test
  public void testGetEmail() throws Throwable {
    Set<String> emails = new TreeSet<String>();

    String path = Thread.currentThread().getContextClassLoader().getResource("bootstrap/question_56405_2235894_1_3").getPath();
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

    try {
      String line = null;
      while ((line = br.readLine()) != null) {
        if (!StringUtils.contains(line, "@")) {
          continue;
        }

        String email = StringUtils.substringBefore(line, ".com") + ".com";
        emails.add(email);
      }

    } finally {
      br.close();
    }

    for (String email : emails) {
      System.out.println(email);
    }
  }

}
