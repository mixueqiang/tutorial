package com.yike.model;

import com.yike.util.ImageUtils;

/**
 * @author xueqiangmi
 * @since May 28, 2015
 */
public class Course extends BaseModel {
  private static final long serialVersionUID = -1007462897232107507L;

  public static final String SQL_TABLE_NAME = "course";
  public static final String SQL_ID = "id";
  public static final String SQL_USER_ID = "userId";
  public static final String SQL_INSTRUCTOR_ID = "instructorId";
  public static final String SQL_TITLE = "name";
  public static final String SQL_CATEGORY_ID = "categoryId";
  public static final String SQL_FREE = "free";
  public static final String SQL_PRICE = "price";
  public static final String SQL_CONTENT = "content";
  public static final String SQL_DESCRIPTION = "description";
  public static final String SQL_TEACHING_TYPE = "teachingType";
  public static final String SQL_IMAGE = "image";
  public static final String SQL_IMAGE_DESCRIPTION = "imageDescription";
  public static final String SQL_IMAGE_QR_CODE = "imageQrCode";
  public static final String SQL_ONLINE_CONTACT_METHOD = "onlineContactMethod";
  public static final String SQL_COUNT_MAX = "countMax";
  public static final String SQL_COUNT_THIS = "countThis";
  public static final String SQL_COUNT_OTHER = "countOther";
  public static final String SQL_COUNT_SHOW = "countShow";
  public static final String SQL_APPLIABLE = "appliable";
  public static final String SQL_CREATE_TIME = "createTime";
  public static final String SQL_UPDATE_TIME = "updateTime";
  public static final String SQL_STATUS = "status";
  public static final String SQL_SUPERSCRIPT = "superscript";
  public static final String SQL_SUBSCRIPT = "subscript";

  public static final int APPLIABLE_TRUE = 1;
  public static final int APPLIABLE_FALSE = -1;

  private long userId;
  private long instructorId;
  private long categoryId;
  // 价格
  private float price;
  // 是否免费
  private int free;
  // 上课方式及时间
  private String teachingType;
  // 总人数
  private int countMax;
  // 一课人数
  private int countThis;
  // 其他人数
  private int countOther;
  // 是否显示总人数
  private boolean countShow;
  // 总人数
  private int count;
  // 招生状态
  private int appliable;
  // 课程亮点
  private String content;
  // 详细介绍
  private String description;
  // 线上联系方式
  private int onlineContactMethod;
  private String image;
  private String imageDescription;
  private String imageQrCode;
  private String subscript; // 下角标
  private String superscript; // 上角标

  // - getters

  public int getAppliable() {
    return appliable;
  }

  public long getCategoryId() {
    return categoryId;
  }

  public String getContent() {
    return content;
  }

  public int getCount() {
    count = getCountThis() + getCountOther();
    return count;
  }

  public int getCountMax() {
    return countMax;
  }

  public int getCountOther() {
    return countOther;
  }

  public int getCountThis() {
    return countThis;
  }

  public String getDescription() {
    return description;
  }

  public int getFree() {
    return free;
  }

  public String getImage() {
    return image;
  }

  public String getImageDescription() {
    return imageDescription;
  }

  public String getImageQrCode() {
    return imageQrCode;
  }

  public String getImageUrl() {
    return ImageUtils.getImageUrl(image);
  }

  public long getInstructorId() {
    return instructorId;
  }

  public int getOnlineContactMethod() {
    return onlineContactMethod;
  }

  public float getPrice() {
    return price;
  }

  public String getSubscript() {
    return subscript;
  }

  public String getSuperscript() {
    return superscript;
  }

  public String getTeachingType() {
    return teachingType;
  }

  public long getUserId() {
    return userId;
  }

  public boolean isCountShow() {
    return countShow;
  }

  // - setters

  public void setAppliable(int appliable) {
    this.appliable = appliable;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setCountMax(int countMax) {
    this.countMax = countMax;
  }

  public void setCountOther(int countOther) {
    this.countOther = countOther;
  }

  public void setCountShow(boolean countShow) {
    this.countShow = countShow;
  }

  public void setCountThis(int countThis) {
    this.countThis = countThis;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setFree(int free) {
    this.free = free;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setImageDescription(String imageDescription) {
    this.imageDescription = imageDescription;
  }

  public void setImageQrCode(String imageQrCode) {
    this.imageQrCode = imageQrCode;
  }

  public void setInstructorId(long instructorId) {
    this.instructorId = instructorId;
  }

  public void setOnlineContactMethod(int onlineContactMethod) {
    this.onlineContactMethod = onlineContactMethod;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public void setSubscript(String subscript) {
    this.subscript = subscript;
  }

  public void setSuperscript(String superscript) {
    this.superscript = superscript;
  }

  public void setTeachingType(String teachingType) {
    this.teachingType = teachingType;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }
}
