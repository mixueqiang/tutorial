package com.yike.web.util;

import com.yike.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;


/**
 * @author ilakeyc
 * @since 09/02/2017
 */
public class WxFotoMixUtil {
  private static final Log LOG = LogFactory.getLog(WxFotoMixUtil.class);

  public static String localImagePath = Constants.IMAGE_REPO + "wx/";

  public static String mainImageName = "wx-invitation-main.jpg";
  public static String localMainImageURL = localImagePath + mainImageName;
  public static String upyunMainImageURL = "http://yikeyun.b0.upaiyun.com/static/" + mainImageName;


//  public static void foo(File file) {
//    BufferedImage image = null;
//    try {
//      image = ImageIO.read(file);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    BufferedImage combined = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
//    Graphics g = combined.getGraphics();
////    g.drawImage();
//    try {
//      ImageIO.write(combined, "JPG", new File(localImagePath, "foo.jpg"));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  private static File getMainImage() {
    File image;
    image = getLocalMainImage();
    if (null == image) {
      try {
        image = SimpleNetworking.downLoadFromUrl(upyunMainImageURL, localImagePath, mainImageName);
      } catch (IOException io) {
        LOG.error("Failure to download image : wx-invitation-main.jpg", io);
      }
    }
    return image;
  }

  private static File getLocalMainImage() {
    File image = new File(localMainImageURL);
    if (!image.exists()) {
      return null;
    }
    return image;
  }

}
