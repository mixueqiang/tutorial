package com.yike.web.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author ilakeyc
 * @since 09/02/2017
 */
public class WxFotoMixUtil {
  public static String path = "pic\\";

  public static void foo(File file) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }

    BufferedImage combined = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics g = combined.getGraphics();
//    g.drawImage();
    try {
      ImageIO.write(combined, "JPG", new File(path, "foo.jpg"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
