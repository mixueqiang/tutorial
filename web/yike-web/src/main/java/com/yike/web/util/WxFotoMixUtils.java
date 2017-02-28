package com.yike.web.util;

import com.yike.Constants;
import com.yike.model.WxUser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author ilakeyc
 * @since 09/02/2017
 */

public class WxFotoMixUtils {
    private static final Log LOG = LogFactory.getLog(WxFotoMixUtils.class);

    public static final String localImagePath = Constants.IMAGE_REPO + "wx/";
//    public static final String localImagePath = "/Users/ilakeyc/Desktop/" + "wx/";

    public static final String mainImageName = "wx-invitation-main-1.jpg";
    public static final String editorQrImageName = "wx_editor_qr.jpeg";
    public static final String zhoumoQrImageName = "wx_zhoumo_qr.jpeg";

    public static final String upyunMainImageURL = "http://yikeyun.b0.upaiyun.com/static/" + mainImageName;
    public static final String upyunEditorQrImageURL = "http://yikeyun.b0.upaiyun.com/static/" + editorQrImageName;
    public static final String upyunZhoumoQrImageURL = "http://yikeyun.b0.upaiyun.com/static/" + zhoumoQrImageName;

    public static File localInvitationImage(String ticket) {
        ticket = ticket.replace("/", "_");
        File file = new File(localImagePath + ticket + ".jpg");
        if (file.exists()) {
            return file;
        }
        return null;
    }

    public static File createInvitationImage(WxUser user, String ticket) {

        File mainImageFile = getMainImage();
        File userImageFile = getUserImage(user);
        File QRCodeFile = getQRCode(ticket);
        String nickName = user.getNickname();
        ticket = ticket.replace("/", "_");
        String imageName = ticket + ".jpg";
        if (StringUtils.length(nickName) > 5) {
            nickName = StringUtils.substring(nickName, 0, 5);
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
                textX = 158;
            }
            if (QRCodeFile != null) {
                QRCode = ImageIO.read(QRCodeFile);
                g2d.drawImage(QRCode, 472, 1002, 210, 210, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            }

            if (StringUtils.isNotEmpty(nickName)) {
                g2d.setColor(Color.white);
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 48));
                g2d.drawString(nickName, textX, 1075);
                g2d.dispose();
            }

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

    public static File getEditorQrCode() {
        return getImage(editorQrImageName, upyunEditorQrImageURL);
    }

    public static File getZhoumoQrCode() {
        return getImage(zhoumoQrImageName, upyunZhoumoQrImageURL);
    }

    private static File getQRCode(String ticket) {
        String qrCodeDownloadUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
        String imageName = encodingFileName(ticket);
        return getImage(imageName, qrCodeDownloadUrl);
    }

    private static File getUserImage(WxUser user) {
        String userAvatarName = encodingFileName(user.getHeadimgurl());
        return getImage(userAvatarName, user.getHeadimgurl());
    }

    private static File getMainImage() {
        return getImage(mainImageName, upyunMainImageURL);
    }

    private static File getImage(String imageName, String imageDownloadUrlString) {
        if (StringUtils.isEmpty(imageName)) {
            LOG.error("unable to get image : fileName could not be `null`");
            return null;
        }
        File image = getLocalImage(imageName);
        return image != null ?
                image :
                SimpleNetworking.downLoadFromUrl(
                        imageDownloadUrlString,
                        localImagePath,
                        imageName);
    }

    private static File getLocalImage(String imageName) {
        File image = new File(localImagePath + imageName);
        if (!image.exists()) {
            return null;
        }
        return image;
    }

    public static String encodingFileName(String baseString) {
        String fileName = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(baseString.getBytes());
            fileName = new BigInteger(1, md.digest()).toString(16) + ".jpg";
        } catch (Throwable t) {
            LOG.error("unable to encoding file name : " + baseString, t);
        }
        return fileName;
    }

}
