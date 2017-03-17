package com.yike.web.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/3/17
 */

public class WxTextResponseUtils {
    private static final Log LOG = LogFactory.getLog(WxTextResponseUtils.class);

    private static Map<String, String> yikeResponses;
    private static Map<String, String> itResponses;

    public static Map<String, String> getYikeResponses() {
        if (yikeResponses == null) {
            yikeResponses = new HashMap<String, String>();
        }
        return yikeResponses;
    }

    public static Map<String, String> getItResponses() {
        if (itResponses == null) {
            itResponses = new HashMap<String, String>();
        }
        return itResponses;
    }

    public static String getYikeResponse(String by) {
        return yikeResponses.get(by);
    }

    public static String getItResponse(String by) {
        return itResponses.get(by);
    }
}
