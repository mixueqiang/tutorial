package com.yike.web.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yike.model.WxUser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.HttpMethod;
import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/2/15
 */
public class WxApiUtils {
    private static final Log LOG = LogFactory.getLog(WxApiUtils.class);

    private String currentAppId;
    private String currentAppSecret;
    public String currentAccessToken;

    public WxApiUtils(String appID, String appSecret) {
        this.currentAppId = appID;
        this.currentAppSecret = appSecret;
    }

    public WxUser requestWxUser(String userOpenId) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + currentAccessToken + "&openid=" + userOpenId + "&lang=zh_CN";
        return getJsonToObject(url, WxUser.class);
    }

    /**
     * @param sceneString 需要二维码附带的业务标识。
     * @return 微信服务器返回的二维码 ticket，用来从微信服务器下载二维码。
     */
    public String requestQRCode(String sceneString) {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + currentAccessToken;

        Map<String, Object> main = new HashMap<String, Object>();
        Map<String, Object> actionInfo = new HashMap<String, Object>();
        Map<String, Object> scene = new HashMap<String, Object>();

        scene.put("scene_str", sceneString);
        scene.put("scene_id", 10000);
        actionInfo.put("scene", scene);
        main.put("action_name", "QR_LIMIT_STR_SCENE");
        main.put("action_info", actionInfo);
        Map<String, String> response = null;
        response = postJsonToObject(url, main, new TypeToken<Map<String, String>>() {
        }.getType());
        return response != null ? response.get("ticket") : "";
    }

    public boolean sendTextMessage(String text, String toUserOpenId) {
        Map<String, Object> messageContent = new HashMap<String, Object>();
        messageContent.put("content", text);
        return sendMessage("text", messageContent, toUserOpenId);
    }

    public boolean sendImageMessage(String mediaId, String toUserOpenId) {
        Map<String, Object> imageContent = new HashMap<String, Object>();
        imageContent.put("media_id", mediaId);
        return sendMessage("image", imageContent, toUserOpenId);
    }

    private boolean sendMessage(String type, Map<String, Object> content, String toUser) {
        String messageSendUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + currentAccessToken;
        Map<String, Object> foo = new HashMap<String, Object>();
        foo.put("touser", toUser);
        foo.put("msgtype", type);
        foo.put(type, content);
        Map<String, String> result = postJsonToObject(
                messageSendUrl,
                foo,
                new TypeToken<Map<String, String>>() {
                }.getType());
        return result != null && "0".equals(result.get("errcode"));
    }

    public boolean requestAccessToken() {

        String urlString = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + currentAppId + "&secret=" + currentAppSecret;
        String result = SimpleNetworking.getRequest(urlString);
        Gson g = new GsonBuilder().disableHtmlEscaping().create();
        Map<String, Object> response = g.fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());
        String token = (String) response.get("access_token");
        this.currentAccessToken = token;
        if (StringUtils.isEmpty(token)) {
            LOG.error("Request access token failure : " + result);
        }
        return StringUtils.isNotEmpty(token);
    }

    /**
     * @param image 本地生成的图片文件。
     * @return 微信服务器返回的 media_id。
     */
    public String uploadTempImage(File image) {
        if (image == null) {
            return null;
        }
        String imageUploadURL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=" + currentAccessToken + "&type=image";
        String responseString = SimpleNetworking.uploadImage(imageUploadURL, image);
        if (StringUtils.isEmpty(responseString)) {
            return "";
        }

        Map<String, String> response = null;
        Gson g = new GsonBuilder().disableHtmlEscaping().create();
        try {
            response = g.fromJson(responseString, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (Throwable t) {
            LOG.error("Json encoding to Object failure", t);
            return "";
        }
        return response.get("media_id");
    }

    public boolean sendTemplateMessage(String templateId, String url, Map<String, Object> data, String toUserOpenId) {
        if (StringUtils.isEmpty(templateId) || StringUtils.isEmpty(toUserOpenId) || data.isEmpty()) {
            LOG.error("Template message id / data / toUserOpenId could not be null");
            return false;
        }
        Map<String, Object> main = new HashMap<String, Object>();
        main.put("touser", toUserOpenId);
        main.put("template_id", templateId);
        if (StringUtils.isNotEmpty(url)) {
            main.put("url", url);
        }
        main.put("data", data);

        String toUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + currentAccessToken;
        Map<String, String> result = postJsonToObject(toUrl, main, new TypeToken<Map<String, String>>() {
        }.getType());
        if (result != null) {
            LOG.info("Template message sent with message id : " + result.get("msgid"));
            return true;
        } else {
            LOG.error("Template message sent failure : " + main.toString());
            return false;
        }
    }

    public <T> T postJsonToObject(String urlString, Object param, Type typeOfT) {
        Gson g = new GsonBuilder().disableHtmlEscaping().create();
        String postJson = null;
        if (param != null) {
            try {
                postJson = g.toJson(param);
            } catch (Throwable t) {
                LOG.error("Object encoding to Json failure", t);
                return null;
            }
        }
        String response = rootRequest(HttpMethod.POST, urlString, postJson);
        if (StringUtils.isEmpty(response)) {
            return null;
        }
        if (typeOfT == null) {
            return null;
        }
        try {
            return g.fromJson(response, typeOfT);
        } catch (Throwable t) {
            LOG.error("Json encoding to Object failure", t);
        }
        return null;
    }

    public <T> T getJsonToObject(String urlString, Type typeOfT) {
        String response = rootRequest(HttpMethod.GET, urlString, null);
        if (response == null) {
            return null;
        }
        Gson g = new GsonBuilder().disableHtmlEscaping().create();
        ;
        try {
            return g.fromJson(response, typeOfT);
        } catch (Throwable t) {
            LOG.error("Json encoding to Object failure", t);
            return null;
        }
    }

    private String rootRequest(String method, String urlString, String param) {
        LOG.info("HTTP request to WX : \n" +
                "method: " + method + "\n" +
                "urlString: " + urlString + "\n" +
                "parameter: " + param);
        if (HttpMethod.GET.equals(method)) {
            String response = SimpleNetworking.getRequest(urlString);
            if (wxResponseError(response)) {
                if (needRequestAccessToken(response)) {
                    requestAccessToken();
                    response = SimpleNetworking.getRequest(urlString);
                }
            }
            return response;
        } else if (HttpMethod.POST.equals(method)) {
            String response = SimpleNetworking.postRequest(urlString, param);
            if (wxResponseError(response)) {
                if (needRequestAccessToken(response)) {
                    requestAccessToken();
                    response = SimpleNetworking.postRequest(urlString, param);
                }
            }
            return response;
        } else {
            return "";
        }
    }

    private boolean wxResponseError(String response) {
        Map<String, String> foo = formateResponseError(response);
        if (foo == null) {
            return false;
        }
        String errorCode = foo.get("errcode");
        String errorMessage = foo.get("errmsg");
        if (StringUtils.isNotEmpty(errorCode)) {
            if (!"0".equals(errorCode)) {
                LOG.info("http request to Wx response an error : " +
                        "\n errorCode=" + errorCode +
                        "\nerrorMessage=" + errorMessage);
            }
        }
        if (StringUtils.isEmpty(errorCode)) {
            return false;
        }
        if ("0".equals(errorCode)) {
            return false;
        }
        return true;
    }

    private boolean needRequestAccessToken(String response) {
        Map<String, String> foo = formateResponseError(response);
        if (foo == null) {
            return false;
        }
        String errorCode = foo.get("errcode");
        String errorMessage = foo.get("errmsg");
        System.out.println("http request to Wx response an error : " +
                "\n errorCode=" + errorCode +
                "\nerrorMessage=" + errorMessage);
        LOG.info("http request to Wx response an error : " +
                "\n errorCode=" + errorCode +
                "\nerrorMessage=" + errorMessage);
        if (StringUtils.isEmpty(errorCode)) {
            return false;
        }
        if ("0".equals(errorCode)) {
            return false;
        }
        return "40013".equals(errorCode) || "40001".equals(errorCode);
    }

    private Map<String, String> formateResponseError(String response) {
        if (StringUtils.isEmpty(response)) {
            return null;
        }
        Gson g = new GsonBuilder().disableHtmlEscaping().create();
        try {
            return g.fromJson(
                    response,
                    new TypeToken<Map<String, String>>() {
                    }.getType());
        } catch (Throwable t) {
            LOG.error("wx response : " + response + " is not a struct like : {\"errcode\":foo,\"errmsg\":\"bar\"}");
        }
        return null;
    }

}
