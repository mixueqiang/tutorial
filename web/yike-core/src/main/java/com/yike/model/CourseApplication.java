package com.yike.model;

/**
 * @author ilakeyc
 * @since 14/12/2016
 */
public class CourseApplication extends BaseModel {
  private static final long serialVersionUID = 8370768493276529936L;

  public static final String SQL_TABLE_NAME = "course_application";
  public static final String SQL_ID = "id";
  public static final String SQL_USER_ID = "userId";
  public static final String SQL_COURSE_ID = "courseId";
  public static final String SQL_NAME = "name";
  public static final String SQL_PHONE = "phone";
  public static final String SQL_QQ = "qq";
  public static final String SQL_WECHAT = "wechat";
  public static final String SQL_PRICE = "price";
  public static final String SQL_ORDER_ID = "orderId";
  public static final String SQL_PROGRESS = "progress";
  public static final String SQL_STATUS = "status";
  public static final String SQL_CREATE_TIME = "createTime";
  public static final String SQL_UPDATE_TIME = "updateTime";

  public static final int PROGRESS_NON_ORDER = 1;
  public static final int PROGRESS_NON_PAYMENT = 2;
  public static final int PROGRESS_PAID = 100;
  public static final int PROGRESS_CANCELED = -100;

  private long userId;
  private long courseId;
  private String name;
  private String phone;
  private String qq;
  private String wechat;
  private float price;
  private String orderId; // 支付订单号，可选
  private int progress;

  public long getCourseId() {
    return courseId;
  }

  public String getName() {
    return name;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getPhone() {
    return phone;
  }

  public float getPrice() {
    return price;
  }

  public int getProgress() {
    return progress;
  }

  public String getQq() {
    return qq;
  }

  public long getUserId() {
    return userId;
  }

  public String getWechat() {
    return wechat;
  }

  public void setCourseId(long courseId) {
    this.courseId = courseId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public void setQq(String qq) {
    this.qq = qq;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public void setWechat(String wechat) {
    this.wechat = wechat;
  }

}
