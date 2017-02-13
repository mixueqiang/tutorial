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
    URL url = new URL(fromUrl);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setConnectTimeout(3 * 1000);
    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

    InputStream inputStream = conn.getInputStream();
    byte[] getData = readInputStream(inputStream);

    File saveDir = new File(savePath);
    if (!saveDir.exists() && saveDir.isDirectory()) {
      saveDir.mkdir();
    }

    File file = new File(saveDir + File.separator + fileName);
    FileOutputStream fos = new FileOutputStream(file);

    fos.write(getData);
    fos.close();

    if (inputStream != null) {
      inputStream.close();
    }
    return new File(savePath + fileName);
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

}
