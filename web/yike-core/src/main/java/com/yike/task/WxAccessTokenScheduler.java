package com.yike.task;

import com.google.gson.Gson;
import com.yike.Constants;
import com.yike.dao.EntityDao;
import com.yike.dao.mapper.WxAccessTokenRowMapper;
import com.yike.model.Entity;
import com.yike.model.WxAccessToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 08/02/2017
 */
@Service
public class WxAccessTokenScheduler implements Runnable {
  private static final Log LOG = LogFactory.getLog(WxAccessTokenScheduler.class);

  private static final String WX_APP_ID = "wxce4aa0af6d3ec704";
  private static final String WX_APP_SECRET = "5f8238027cab1b5348df2dd86f5bd6fe";

  @Resource
  protected EntityDao entityDao;

  public WxAccessTokenScheduler() {

    TaskScheduler.register(getClass().getSimpleName(), this, 10, 7100);
  }

  @Override
  public void run() {

    requestAccessToken();
    checkEnabledCurrentAccessToken();

  }

  private void checkEnabledCurrentAccessToken() {
    Map<String, Object> tokenFindCondition = new HashMap<String, Object>();
    tokenFindCondition.put("status", Constants.STATUS_OK);
    long currentTime = System.currentTimeMillis();
    List<WxAccessToken> tokens = entityDao.find("wx_access_token", tokenFindCondition, WxAccessTokenRowMapper.getInstance());
    for (WxAccessToken token : tokens) {
      long createTime = token.getCreateTime();
      if ((createTime - currentTime) / 1000 > 7100) {
        Map<String, Object> tokenUpdateCondition = new HashMap<String, Object>();
        tokenUpdateCondition.put("id", token.getId());
        Map<String, Object> tokenUpdateValues = new HashMap<String, Object>();
        tokenUpdateValues.put("status", Constants.STATUS_NO);
        try {
          entityDao.update("wx_access_token", tokenUpdateCondition, tokenUpdateValues);
        } catch (Throwable t) {
          LOG.error("unable to update old access_token from TECENT", t);
        }
      }
    }
  }

  protected void requestAccessToken() {

    String urlString = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WX_APP_ID + "&secret=" + WX_APP_SECRET;

    String result = getRequest(urlString);

    Gson gson = new Gson();
    WxAccessToken token = gson.fromJson(result, WxAccessToken.class);

    String accessToken = token.getAccess_token();
    long expiresIn = token.getExpires_in();
    long createTime = System.currentTimeMillis();

    try {

      Entity entity = new Entity("wx_access_token");

      entity.set("access_token", accessToken);
      entity.set("expires_in", expiresIn);
      entity.set("createTime", createTime);
      entity.set("status", Constants.STATUS_OK);

      entityDao.save(entity);

    } catch (Throwable t) {
      t.printStackTrace();
      LOG.error("unable to save access_token from TECENT", t);
    }
  }

  protected String getRequest(String urlString) {

    String result = "";
    BufferedReader in = null;

    try {

      URL url = new URL(urlString);

      URLConnection connection = url.openConnection();
      connection.setRequestProperty("accept", "application/json; charset=utf-8");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty("user-agent",
              "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

      connection.connect();

      Map<String, java.util.List<String>> map = connection.getHeaderFields();
      for (String key : map.keySet()) {
        in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
          result += line;
        }
      }

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    return result;
  }

}
