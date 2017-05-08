package com.yike.web.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yike.model.QQUserInfo;
import com.yike.util.HttpClientUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/3/24
 */
public class QQAuthUtils {
    private static final Log LOG = LogFactory.getLog(QQAuthUtils.class);

    private static final String APP_ID = "101392056";
    private static final String APP_SECRET = "4041ee3cae2feba34e87e2aa12680789";
    private static final String REDIRECT_URI = "http%3A%2F%2Fwww.yikeshangshou.com%2Fapi%2Fv1%2Fauth%2Fqq";

    public static String getAuthUrl() {
        return "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + APP_ID +
                "&redirect_uri=" + REDIRECT_URI;
    }

    /**
     * @param code 通过回调地址传回的 Authorization Code
     * @return 请求是否发送成功
     * QQ 会向 redirect_url 发送带有 access_token 和 expires_in 的请求
     */
    public static String requestAccessTokenByCode(String code) {

        String result;

        String url = "https://graph.qq.com/oauth2.0/token?" +
                "grant_type=authorization_code" +
                "&client_id=" + APP_ID +
                "&client_secret=" + APP_SECRET +
                "&code=" + code +
                "&state=" + "aaaaaaaaaaaaaaaaaaaaaaa" +
                "&redirect_uri=" + REDIRECT_URI;
        try {
            result = HttpClientUtils.get(url);
            LOG.info("Get request to QQ : " + url + ", response: " + result);
            String[] keys = StringUtils.split(result, "&");
            for (String key : keys) {
                if (StringUtils.contains(key, "access_token")) {
                    return StringUtils.replace(key, "access_token=", "");
                }
            }
        } catch (Throwable t) {
            LOG.error("Get AccessTokenContainer By AuthorizationCode Failure", t);
        }
        return null;
    }

    public static String getOpenId(String accessToken) {

        String result;

        String url = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken;

        try {
            result = HttpClientUtils.get(url);
            result = result.replace("callback( ", "");
            result = result.replace(" );", "");
        } catch (Throwable t) {
            LOG.error("Get OpenId By AccessTokenContainer Failure", t);
            return null;
        }
        LOG.info("Get request to QQ : " + url + ", response: " + result);

        if (hasError(result)) {
            return null;
        }

        Gson gson = new Gson();
        String openId = null;
        try {
            Map<String, String> oidMap = gson.fromJson(result, new TypeToken<Map<String, String>>() {
            }.getType());
            openId = oidMap.get("openid");
        } catch (Throwable t) {
            LOG.error("Encoding json to Map<String, String> failure", t);
        }
        return openId;
    }

    public static QQUserInfo getUserInfo(String token, String openId) {

        String result;

        String url = "https://graph.qq.com/user/get_user_info?" +
                "access_token=" + token +
                "&oauth_consumer_key=" + APP_ID +
                "&openid=" + openId;
        try {
            result = HttpClientUtils.get(url);
        } catch (Throwable t) {
            LOG.error("Get UserInfo By AccessTokenContainer Failure", t);
            return null;
        }
        LOG.info("Get request to QQ : " + url + ", response: " + result);
        if (hasError(result)) {
            return null;
        }

        Gson gson = new Gson();
        QQUserInfo userInfo = null;
        try {
            userInfo = gson.fromJson(result, QQUserInfo.class);
        } catch (Throwable t) {
            LOG.error("Encoding json to WechatUserInfo failure", t);
        }
        return userInfo;
    }

    private static boolean hasError(String result) {
        Gson gson = new Gson();
        try {
            Map<String, String> error = gson.fromJson(result, new TypeToken<Map<String, String>>() {
            }.getType());
            String code = error.get("error");
            String message = error.get("error_description");
            if (StringUtils.isNotEmpty(code) && !"0".equals(code)) {
                LOG.info("QQ response an error : " + result);
                return true;
            } else {
                return false;
            }
        } catch (Throwable t) {
            LOG.error("Encoding json string to Map<String, String> failure", t);
            return false;
        }
    }

}
