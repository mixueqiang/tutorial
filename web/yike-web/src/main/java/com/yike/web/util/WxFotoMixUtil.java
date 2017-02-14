package com.yike.web.util;

import com.yike.Constants;
import com.yike.model.WxUser;
import com.yike.service.WxService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;


/**
 * @author ilakeyc
 * @since 09/02/2017
 */
public class WxFotoMixUtil {
  private static final Log LOG = LogFactory.getLog(WxFotoMixUtil.class);

  public static String localImagePath = Constants.IMAGE_REPO + "wx/";

  public static String mainImageName = "wx-invitation-main.jpg";

  public static String upyunMainImageURL = "http://yikeyun.b0.upaiyun.com/static/" + mainImageName;

  public static File createInvitationImage(WxUser user) {
    File mainImageFile = getMainImage();
    File userImageFile = getUserImage(user);
    File QRCodeFile = getQRCode(user);
    String nickName = user.getNickname();
    if (StringUtils.length(nickName) > 5) {
      nickName = StringUtils.substring(nickName, 0, 4);
    }
    try {
      Image mainImage = ImageIO.read(mainImageFile);
      Image userImage;
      Image QRCode;

      BufferedImage tag = new BufferedImage(750, 1334, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2d = tag.createGraphics();
      g2d.drawImage(mainImage, 0, 0, 750, 1334, null);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
      float textX = 49;
      if (userImageFile != null) {
        userImage = ImageIO.read(userImageFile);
        g2d.drawImage(userImage, 49, 1020, 77, 77, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
      } else {
        textX = 158;
      }
      if (QRCodeFile != null) {
        QRCode = ImageIO.read(QRCodeFile);
        g2d.drawImage(QRCode, 474, 1002, 210, 210, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
      }

      g2d.setColor(Color.white);
      g2d.setFont(new Font(null, Font.BOLD, 12));
      g2d.drawString(nickName, textX, 1035);
      g2d.dispose();

      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(user.getOpenid().getBytes());
      String imageName = new BigInteger(1, md.digest()).toString(16) + "wx.jpg";
      String imageSaveUrl = localImagePath + imageName;
      FileOutputStream out = new FileOutputStream(imageSaveUrl);
      ImageIO.write(tag, "jpg", out);
      out.close();

      return getLocalImage(imageName);
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return null;
  }

  private static File getQRCode(WxUser user) {
    File image;
    String imageName;
    try {

      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(user.getOpenid().getBytes());
      imageName = new BigInteger(1, md.digest()).toString(16) + ".jpg";

      image = getLocalImage(imageName);

      if (null == image) {
        String ticket = WxService.requestQRCode(imageName);
        image = SimpleNetworking.downLoadFromUrl("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket, localImagePath, imageName);
      }

      return image;

    } catch (Throwable t) {
      LOG.error("get WxUser avatar name unsuccessful", t);
    }

    return null;
  }


  private static File getUserImage(WxUser user) {
    File image;
    String userAvatarName;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(user.getHeadimgurl().getBytes());
      userAvatarName = new BigInteger(1, md.digest()).toString(16);

      image = getLocalImage(userAvatarName);

      if (null == image) {
        image = SimpleNetworking.downLoadFromUrl(user.getHeadimgurl(), localImagePath, userAvatarName);
      }

      return image;

    } catch (Throwable t) {
      LOG.error("get WxUser avatar name unsuccessful", t);
    }
    return null;
  }


  private static File getMainImage() {
    File image;
    image = getLocalImage(mainImageName);
    if (null == image) {
      try {
        image = SimpleNetworking.downLoadFromUrl(upyunMainImageURL, localImagePath, mainImageName);
      } catch (IOException io) {
        LOG.error("Failure to download image : wx-invitation-main.jpg", io);
      }
    }
    return image;
  }

  private static File getLocalImage(String imageName) {
    File image = new File(localImagePath + imageName);
    if (!image.exists()) {
      return null;
    }
    return image;
  }

}
