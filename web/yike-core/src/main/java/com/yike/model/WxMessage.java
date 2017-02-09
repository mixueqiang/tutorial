package com.yike.model;

import java.io.Serializable;

import org.w3c.dom.Element;

/**
 * @author ilakeyc
 * @since 09/02/2017
 */
public class WxMessage implements Serializable {
  private static final long serialVersionUID = -5203791270579103906L;


  private String toUserName;   // all 开发者微信号
  private String fromUserName; // all 发送方帐号（一个OpenID）
  private long createTime;     // all 消息创建时间 （整型）
  private String msgId;        // all 消息id，64位整型
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
  private String msgType;
  private String content; // text 文本消息内容
  private String picUrl;  // image 图片链接（由系统生成）
  // voice 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
  // video 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
  private String mediaId; // image 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
  private String format;       // voice 语音格式，如amr，speex等
  private String recognition;  // voice 语音识别结果，UTF8编码
  private String thumbMediaId; // video 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
  private double location_X;   // location 地理位置维度
  private double location_Y;   // location 地理位置经度
  private double scale;        // location 地图缩放大小
  private String label;        // location 地理位置信息
  private String title;        // link 消息标题
  private String description;  // link 消息描述
  private String url;          // link 消息链接

  /**
   * 事件类型
   * subscribe/unsubscribe --- 关注/取消关注
   * subscribe --------------- 扫描带参数二维码事件（未关注）
   * SCAN -------------------- 扫描带参数二维码事件（已关注）
   * LOCATION ---------------- 上报地理位置事件
   * CLICK ------------------- 点击菜单拉取消息时的事件推送
   * VIEW -------------------- 点击菜单跳转链接时的事件推送
   * */
  private String event;
  // SCAN  事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
  // CLICK 事件KEY值，与自定义菜单接口中KEY值对应
  // VIEW  事件KEY值，设置的跳转URL
  private String eventKey; // subscribe 事件KEY值，qrscene_为前缀，后面为二维码的参数值。
  private String ticket;   // subscribe 二维码的ticket，可用来换取二维码图片
  private String latitude; // LOCATION
  private String longitude;// LOCATION
  private String precision;// LOCATION 地理位置精度

  private Element xmlRoot;
  public WxMessage(Element xmlRoot) {
    this.xmlRoot = xmlRoot;
    setToUserName(getContext("ToUserName"));
    setFromUserName(getContext("FromUserName"));
    setCreateTime(Long.parseLong(getContext("CreateTime")));
    setMsgId(getContext("MsgId"));
    setMsgType(getContext("MsgType"));
    setContent(getContext("Content"));
    setPicUrl(getContext("PicUrl"));
    setMediaId(getContext("MediaId"));
    setFormat(getContext("Format"));
    setRecognition(getContext("Recognition"));
    setThumbMediaId(getContext("ThumbMediaId"));
    setLocation_X(Double.parseDouble(getContext("Location_X")));
    setLocation_Y(Double.parseDouble(getContext("Location_Y")));
    setScale(Double.parseDouble(getContext("Scale")));
    setLabel(getContext("Label"));
    setTitle(getContext("Title"));
    setDescription(getContext("Description"));
    setUrl(getContext("Url"));
  }

  private String getContext(String name) {
    return xmlRoot.getElementsByTagName(name)
            .item(0).getTextContent();
  }

  public String getToUserName() {
    return toUserName;
  }

  public void setToUserName(String toUserName) {
    this.toUserName = toUserName;
  }

  public String getFromUserName() {
    return fromUserName;
  }

  public void setFromUserName(String fromUserName) {
    this.fromUserName = fromUserName;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public String getPicUrl() {
    return picUrl;
  }

  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }

  public String getMediaId() {
    return mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getRecognition() {
    return recognition;
  }

  public void setRecognition(String recognition) {
    this.recognition = recognition;
  }

  public String getThumbMediaId() {
    return thumbMediaId;
  }

  public void setThumbMediaId(String thumbMediaId) {
    this.thumbMediaId = thumbMediaId;
  }

  public double getLocation_X() {
    return location_X;
  }

  public void setLocation_X(double location_X) {
    this.location_X = location_X;
  }

  public double getLocation_Y() {
    return location_Y;
  }

  public void setLocation_Y(double location_Y) {
    this.location_Y = location_Y;
  }

  public double getScale() {
    return scale;
  }

  public void setScale(double scale) {
    this.scale = scale;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getEventKey() {
    return eventKey;
  }

  public void setEventKey(String eventKey) {
    this.eventKey = eventKey;
  }

  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getPrecision() {
    return precision;
  }

  public void setPrecision(String precision) {
    this.precision = precision;
  }
}
