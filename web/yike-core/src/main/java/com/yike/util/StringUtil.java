package com.yike.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author mixueqiang
 * @since Sep 28, 2016
 *
 */
public final class StringUtil {
  private static final int LENGTH_MEDIUM = 100;

  public static String getPreview(String source) {
    return getPreview(source, LENGTH_MEDIUM);
  }

  public static String getPreview(String source, int length) {
    if (StringUtils.isEmpty(source)) {
      return null;
    }

    length = length >= 20 ? length : 20;
    if (StringUtils.length(source) <= length) {
      return StringUtils.replaceEach(source, new String[] { "\"", "'" }, new String[] { " ", " " });
    }

    String preview = StringUtils.substring(source, 0, length);
    if (StringUtils.indexOf(preview, " ") > (length - 20)) {
      preview = StringUtils.substringBeforeLast(preview, " ") + "...";
    }

    return StringUtils.replaceEach(preview, new String[] { "\"", "'" }, new String[] { " ", " " });
  }

  public static String replaceNewLine(String content) {
    if (StringUtils.isEmpty(content)) {
      return content;
    }

    return StringUtils.replace(content, "\n", "<br/>");
  }

}
