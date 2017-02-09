package com.yike.web.api.v1;

import com.yike.model.WxMessage;
import com.yike.util.XmlUtil;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.yike.service.WxService.WX_TOKEN;

/**
 * @author ilakeyc
 * @since 07/02/2017
 */

@Path("/api/v1/wx")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiWxResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiCourseResource.class);

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String test(
          @DefaultValue("") @QueryParam("signature") String signature,
          @DefaultValue("") @QueryParam("timestamp") String timestamp,
          @DefaultValue("") @QueryParam("nonce") String nonce,
          @DefaultValue("") @QueryParam("echostr") String echostr) {
    return echostr;
  }

  @POST
  @Consumes(MediaType.TEXT_XML)
  @Produces(MediaType.TEXT_XML)
  public String receiveMessages(String xml) {
    System.out.println(xml);

    WxMessage message = handleMessage(xml);

    String response = "<xml>" +
            "<ToUserName><![CDATA[" + message.getFromUserName() + "]]></ToUserName>" +
            "<FromUserName><![CDATA[" + message.getToUserName() + "]]></FromUserName>" +
            "<CreateTime>" + message.getCreateTime() + "</CreateTime>" +
            "<MsgType><![CDATA[text]]></MsgType>" +
            "<Content><![CDATA[" + message.getMsgType() + "]]></Content>" +
            "</xml>";
    System.out.println(response);
    return response;
  }


  private WxMessage handleMessage(String xmlString) {

    WxMessage message = null;
    InputSource in = new InputSource(new StringReader(xmlString));
    in.setEncoding("UTF-8");
    SAXReader reader = new SAXReader();
    Document document;
    try {
      document = reader.read(in);
      Element root = document.getRootElement();
      message = (WxMessage) XmlUtil.fromXmlToBean(root, WxMessage.class);

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("数据解析错误");
    }
    return message;
  }

  private boolean check_signature(String signature, String timestamp, String nonce) {
    String[] array = new String[]{timestamp, nonce, WX_TOKEN};
    Arrays.sort(array);
    String sortedString = array[0] + array[1] + array[2];
    String hexString;
    MessageDigest md;

    try {
      md = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException nsae) {
      nsae.printStackTrace();
      return false;
    }

    try {
      byte[] messageDigest = md.digest(sortedString.getBytes("utf-8"));
      StringBuilder sb = new StringBuilder();
      for (byte aMessageDigest : messageDigest) {
        String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
        if (shaHex.length() < 2) {
          sb.append(0);
        }
        sb.append(shaHex);
      }
      hexString = sb.toString();
      return StringUtils.equals(signature, hexString);
    } catch (UnsupportedEncodingException uee) {
      uee.printStackTrace();
      return false;
    }
  }


}
