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
    mParms = DigestUtils.sha1Hex(parmsString);

    if (StringUtils.equals(mParms, signature)) {
      return echostr;
    }

    return "";
  }


}
