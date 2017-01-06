package com.yike.web.api.v1;

import com.yike.model.User;
import com.yike.util.Pair;
import com.yike.util.ResponseBuilder;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.yike.util.ImageUtils;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mixueqiang
 * @since Jun 11, 2016
 */
@Path("/api/v1/image")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiImageResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiImageResource.class);

  @GET
  @Produces(APPLICATION_JSON)
  public Map<String, Object> getAndSaveImage(@QueryParam("imageUrl") String imageUrl) {
    User user = getSessionUser();
    if (user == null) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    if (!StringUtils.endsWithIgnoreCase(imageUrl, "png") && !StringUtils.endsWithIgnoreCase(imageUrl, "jpg") && !StringUtils.endsWithIgnoreCase(imageUrl, "jpeg")
        && !StringUtils.endsWithIgnoreCase(imageUrl, "gif")) {
      return ResponseBuilder.error(50000, "仅支持后缀为png、jpg、jpeg或gif的图片。");
    }

    try {
      Pair<String, String> imagePath = ImageUtils.getAndSaveImage(imageUrl);
      String path = imagePath.left + "/" + imagePath.right;
      LOG.info("File " + imageUrl + " saved to: " + path);

      Map<String, String> result = new HashMap<String, String>();
      result.put("path", path);
      result.put("url", ImageUtils.getImageUrl(path));
      return ResponseBuilder.ok(result);

    } catch (Throwable t) {
      LOG.warn("Error occurs on reading image.", t);
      return ResponseBuilder.error(50000, "获取图片失败。");
    }
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> save(@FormDataParam("imageFile") InputStream inputStream, @FormDataParam("imageFile") FormDataContentDisposition fileDetails) {
    User user = getSessionUser();
    if (user == null) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    if (inputStream == null || StringUtils.isEmpty(fileDetails.getFileName())) {
      return ResponseBuilder.error(50000, "请上传一张图片。");
    }

    try {
      // Save an image.
      String fileName = fileDetails.getFileName();
      Pair<String, String> filePath = ImageUtils.saveAndUpload(inputStream, fileName);
      String path = filePath.left + "/" + filePath.right;
      LOG.info("File " + fileName + " saved to: " + path);

      Map<String, String> result = new HashMap<String, String>();
      result.put("path", path);
      result.put("url", ImageUtils.getImageUrl(path));
      return ResponseBuilder.ok(result);

    } catch (Throwable t) {
      LOG.warn("Error occurs on saving image.", t);
      return ResponseBuilder.error(50000, "Error occurs on uploading and save an image.");
    }
  }

}
