package com.yike.web.api.v1;

import com.yike.web.BaseResource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
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
public class ApiWXResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiCourseResource.class);

  private static final String WX_TOKEN = "yikeshangshouwx";

  @GET
  public String test(
          @MatrixParam("signature") String signature,
          @MatrixParam("timestamp") String timestamp,
          @MatrixParam("nonce") String nonce,
          @MatrixParam("echostr") String echostr) {

    String[] parms = new String[]{WX_TOKEN, timestamp, nonce};
    Arrays.sort(parms);
    String parmsString = "";

    for (int i = 0; i < parms.length; i++) {
      parmsString += parms[i];
    }

    String mParms = null;
    MessageDigest digest = null;
    try {
      digest = java.security.MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    digest.update(parmsString.getBytes());
    byte messageDigest[] = digest.digest();

    StringBuffer hexString = new StringBuffer();

    for (int i = 0; i < messageDigest.length; i++) {
      String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
      if (shaHex.length() < 2) {
        hexString.append(0);
      }
      hexString.append(shaHex);
    }
    mParms = hexString.toString();

    if (StringUtils.equals(mParms, signature)) {
      return echostr;
    }

    return "";
  }


}
