package com.yike.model;

import java.io.Serializable;

/**
 * @author ilakeyc
 * @since 09/02/2017
 */
public class WxMessage implements Serializable {
  private static final long serialVersionUID = -5203791270579103906L;


  private String ToUserName;   // all 开发者微信号
  private String FromUserName; // all 发送方帐号（一个OpenID）
  private long CreateTime;     // all 消息创建时间 （整型）
  private String MsgId;        // all 消息id，64位整型
  /**
   * 消息类型
   * text  ------ 文本消息
   * image ------ 图片消息
   * voice ------ 语音消息
   * video ------ 视频消息
   * shortvideo - 小视频消息
   * location --- 位置消息
   * link ------- 链接消息
   * event ------ 事件
   */
  private String MsgType;
  private String Content; // text 文本消息内容
  private String PicUrl;  // image 图片链接（由系统生成）
  // voice 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
  // video 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
  private String MediaId; // image 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
  private String Format;       // voice 语音格式，如amr，speex等
  private String Recognition;  // voice 语音识别结果，UTF8编码
  private String ThumbMediaId; // video 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
  private double Location_X;   // location 地理位置维度
  private double Location_Y;   // location 地理位置经度
  private double Scale;        // location 地图缩放大小
  private String Label;        // location 地理位置信息
  private String Title;        // link 消息标题
  private String Description;  // link 消息描述
  private String Url;          // link 消息链接

  /**
   * 事件类型
   * subscribe/unsubscribe --- 关注/取消关注
   * subscribe --------------- 扫描带参数二维码事件（未关注）
   * SCAN -------------------- 扫描带参数二维码事件（已关注）
   * LOCATION ---------------- 上报地理位置事件
   * CLICK ------------------- 点击菜单拉取消息时的事件推送
   * VIEW -------------------- 点击菜单跳转链接时的事件推送
   */
  private String Event;
  // SCAN  事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
  // CLICK 事件KEY值，与自定义菜单接口中KEY值对应
  // VIEW  事件KEY值，设置的跳转URL
  private String EventKey; // subscribe 事件KEY值，qrscene_为前缀，后面为二维码的参数值。
  private String Ticket;   // subscribe 二维码的ticket，可用来换取二维码图片
  private String Latitude; // LOCATION
  private String Longitude;// LOCATION
  private String Precision;// LOCATION 地理位置精度


  public String getToUserName() {
    return ToUserName;
  }

  public void setToUserName(String toUserName) {
    ToUserName = toUserName;
  }

  public String getFromUserName() {
    return FromUserName;
  }

  public void setFromUserName(String fromUserName) {
    FromUserName = fromUserName;
  }

  public long getCreateTime() {
    return CreateTime;
  }

  public void setCreateTime(long createTime) {
    CreateTime = createTime;
  }

  public String getMsgId() {
    return MsgId;
  }

  public void setMsgId(String msgId) {
    MsgId = msgId;
  }

  public String getMsgType() {
    return MsgType;
  }

  public void setMsgType(String msgType) {
    MsgType = msgType;
  }

  public String getContent() {
    return Content;
  }

  public void setContent(String content) {
    Content = content;
  }

  public String getPicUrl() {
    return PicUrl;
  }

  public void setPicUrl(String picUrl) {
    PicUrl = picUrl;
  }

  public String getMediaId() {
    return MediaId;
  }

  public void setMediaId(String mediaId) {
    MediaId = mediaId;
  }

  public String getFormat() {
    return Format;
  }

  public void setFormat(String format) {
    Format = format;
  }

  public String getRecognition() {
    return Recognition;
  }

  public void setRecognition(String recognition) {
    Recognition = recognition;
  }

  public String getThumbMediaId() {
    return ThumbMediaId;
  }

  public void setThumbMediaId(String thumbMediaId) {
    ThumbMediaId = thumbMediaId;
  }

  public double getLocation_X() {
    return Location_X;
  }

  public void setLocation_X(double location_X) {
    Location_X = location_X;
  }

  public double getLocation_Y() {
    return Location_Y;
  }

  public void setLocation_Y(double location_Y) {
    Location_Y = location_Y;
  }

  public double getScale() {
    return Scale;
  }

  public void setScale(double scale) {
    Scale = scale;
  }

  public String getLabel() {
    return Label;
  }

  public void setLabel(String label) {
    Label = label;
  }

  public String getTitle() {
    return Title;
  }

  public void setTitle(String title) {
    Title = title;
  }

  public String getDescription() {
    return Description;
  }

  public void setDescription(String description) {
    Description = description;
  }

  public String getUrl() {
    return Url;
  }

  public void setUrl(String url) {
    Url = url;
  }

  public String getEvent() {
    return Event;
  }

  public void setEvent(String event) {
    Event = event;
  }

  public String getEventKey() {
    return EventKey;
  }

  public void setEventKey(String eventKey) {
    EventKey = eventKey;
  }

  public String getTicket() {
    return Ticket;
  }

  public void setTicket(String ticket) {
    Ticket = ticket;
  }

  public String getLatitude() {
    return Latitude;
  }

  public void setLatitude(String latitude) {
    Latitude = latitude;
  }

  public String getLongitude() {
    return Longitude;
  }

  public void setLongitude(String longitude) {
    Longitude = longitude;
  }

  public String getPrecision() {
    return Precision;
  }

  public void setPrecision(String precision) {
    Precision = precision;
  }
}
