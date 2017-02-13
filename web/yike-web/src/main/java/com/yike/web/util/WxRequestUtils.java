package com.yike.web.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yike.model.WxRequestResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/2/13
 */
public class WxRequestUtils {
  private static final Log LOG = LogFactory.getLog(WxRequestUtils.class);

  public static <T> WxRequestResponse<T> getJson(String urlString, Type typeOfT) {
    Map<String, String> error;
    String errorCode;
    String errorMessage;
    String responseString;
    WxRequestResponse<T> response;

    responseString = SimpleNetworking.getRequest(urlString);

    Gson g = new Gson();

    error = g.fromJson(responseString, new TypeToken<Map<String, String>>(){}.getType());
    errorCode = error.get("errcode");
    errorMessage = error.get("errorMessage");

    response = new WxRequestResponse<T>();

    if (!StringUtils.isEmpty(errorCode) && !"0".equals(errorCode)) {
      response.setErrorCode(errorCode);
      response.setErrorMessage(errorMessage);
      LOG.error(urlString + "\n" + "errorCode: " + errorCode + "errorMessage: " + errorMessage);
      return response;
    }

    T object = g.fromJson(responseString, typeOfT);
    response.setObject(object);

    return response;
  }
}
