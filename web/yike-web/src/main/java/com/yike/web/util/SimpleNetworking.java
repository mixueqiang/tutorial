package com.yike.web.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 09/02/2017
 */
public class SimpleNetworking {

  public static String getRequest(String urlString) {

    String result = "";
    BufferedReader in = null;

    try {

      URL url = new URL(urlString);

      URLConnection connection = url.openConnection();
      connection.setRequestProperty("accept", "application/json; charset=utf-8");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty("user-agent",
              "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

      connection.connect();

      Map<String, List<String>> map = connection.getHeaderFields();
      for (String key : map.keySet()) {
        in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
          result += line;
        }
      }

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    return result;
  }

  public static String postRequest(String url, Object param) {
    PrintWriter out = null;
    BufferedReader in = null;
    String result = "";
    try {
      URL realUrl = new URL(url);
      URLConnection conn = realUrl.openConnection();
      conn.setRequestProperty("accept", "application/json; charset=utf-8");
      conn.setDoOutput(true);
      conn.setDoInput(true);
      out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
      System.out.println(param);
      out.print(param);
      out.flush();
      in = new BufferedReader(
              new InputStreamReader(conn.getInputStream(), "utf-8"));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return result;
  }

  public static File downLoadFromUrl(String fromUrl, String savePath, String fileName) throws IOException {
    // 下载网络文件
    int bytesum = 0;
    int byteread = 0;

    URL url = new URL(fromUrl);

    try {
      URLConnection conn = url.openConnection();
      InputStream inStream = conn.getInputStream();
      File saveDir = new File(savePath);
      if (!saveDir.exists()) {
        saveDir.mkdir();
      }
      FileOutputStream fs = new FileOutputStream(savePath + fileName);

      byte[] buffer = new byte[1204];
      int length;
      while ((byteread = inStream.read(buffer)) != -1) {
        bytesum += byteread;
        fs.write(buffer, 0, byteread);
      }
      File file = new File(savePath + fileName);
      return file;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
//    URL url = new URL(fromUrl);
//    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//    conn.setConnectTimeout(3 * 1000);
//    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//
//    InputStream inputStream = conn.getInputStream();
//    byte[] getData = readInputStream(inputStream);
//
//    File saveDir = new File(savePath);
//    if (!saveDir.exists() && saveDir.isDirectory()) {
//      saveDir.mkdir();
//    }
//
//    File file = new File(saveDir + File.separator + fileName);
//    FileOutputStream fos = new FileOutputStream(file);
//
//    fos.write(getData);
//    fos.close();
//
//    if (inputStream != null) {
//      inputStream.close();
//    }
//    return new File(savePath + fileName);
  }

  private static byte[] readInputStream(InputStream inputStream) throws IOException {
    byte[] buffer = new byte[1024];
    int len = 0;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while ((len = inputStream.read(buffer)) != -1) {
      bos.write(buffer, 0, len);
    }
    bos.close();
    return bos.toByteArray();
  }

  public static String uploadImage(String url, File image) {
    BufferedReader reader = null;
    String result = "";
    try {
      URL realUrl = new URL(url);
      URLConnection conn = realUrl.openConnection();
      StringBuilder sb = new StringBuilder();

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);

      conn.setRequestProperty("Connection", "Keep-Alive");
      conn.setRequestProperty("Charset", "UTF-8");

      String BOUNDARY = "----------" + System.currentTimeMillis();
      conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);


      sb.append("--"); // 必须多两道线

      sb.append(BOUNDARY);

      sb.append("\r\n");

      sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + image.length() + "\";filename=\""

              + image.getName() + "\"\r\n");

      sb.append("Content-Type:application/octet-stream\r\n\r\n");

      byte[] head = sb.toString().getBytes("utf-8");

      OutputStream out = new DataOutputStream(conn.getOutputStream());

      out.write(head);

      DataInputStream in = new DataInputStream(new FileInputStream(image));

      int bytes = 0;

      byte[] bufferOut = new byte[1024];

      while ((bytes = in.read(bufferOut)) != -1) {
        out.write(bufferOut, 0, bytes);
      }

      in.close();

      byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线

      out.write(foot);
      out.flush();
      out.close();

      StringBuffer buffer = new StringBuffer();
      reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

      String line = null;

      while ((line = reader.readLine()) != null) {
        buffer.append(line);
      }

      return result;

    } catch (IOException o) {

      return null;

    } finally {

      if (reader != null) {
        try {
          reader.close();
        } catch (IOException o) {
          o.printStackTrace();

        }
      }

    }
  }
}