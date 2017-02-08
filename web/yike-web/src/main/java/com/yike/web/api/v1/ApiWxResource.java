package com.yike.web.api.v1;

import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author ilakeyc
 * @since 07/02/2017
 */

@Path("/api/v1/wx")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiWxResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiCourseResource.class);

  private static final String WX_TOKEN = "yikeshangshouwx";

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
  @Produces(MediaType.TEXT_XML)
  public String handleMessages(
          @FormParam("ToUserName") String toUserName,
          @FormParam("FromUserName") String fromUserName,
          @FormParam("CreateTime") long createTime,
          @FormParam("MsgType") String msgType,
          @FormParam("Content") String content,
          @FormParam("MsgId") long msgId) {
    System.out.println(msgId);
    return null;
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
