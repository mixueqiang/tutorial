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
    public static final String SQL_IMAGE = "image";
    public static final String SQL_FREE = "free";
    public static final String SQL_PRICE = "price";
    public static final String SQL_TEACHING_TYPE = "teachingType";
    public static final String SQL_CONTENT = "content";
    public static final String SQL_DESCRIPTION = "description";
    public static final String SQL_LEARNER_COUNT = "maximumLearnerCount";
    public static final String SQL_CURRENT_LEARNER_COUNT = "currentLearnerCount";
    public static final String SQL_OTHER_LEARNER_COUNT = "otherLearnerCount";
    public static final String SQL_SHOW_TOTAL_LEARNER_COUNT = "showTotalLearnerCount";
    public static final String SQL_APPLIABLE = "appliable";
    public static final String SQL_CREATE_TIME = "createTime";
    public static final String SQL_UPDATE_TIME = "updateTime";
    public static final String SQL_STATUS = "status";
    public static final String SQL_SUPERSCRIPT = "superscript";
    public static final String SQL_SUBSCRIPT = "subscript";


    public static final int APPLIABLE_TRUE = 1;
    public static final int APPLIABLE_FALSE = -1;

    private long userId;   // 用户ID
    private long instructorId;// 讲师ID

    private float price;        // 价格         <100000
    private int free;           // 是否免费      1 = Y
    private String teachingType;// 上课方式及时间  2000 字

    private int maximumLearnerCount;// 最大人数   <10000
    private int currentLearnerCount;// 当前人数
    private int otherLearnerCount;
    private int totalLearnerCount;  // 报名总数
    private int showTotalLearnerCount;

    private String description; // 详细介绍         3000 字
    private String content;     // 课程亮点/核心内容 150  字

    private int appliable;      // 招生状态 (可申请/已停止招生)

    private String image;

    private String subscript; // 下角标
    private String superscript; // 上角标

    // - getters

    public long getUserId() {
        return userId;
    }

    public long getInstructorId() {
        return instructorId;
    }

    public float getPrice() {
        return price;
    }

    public int getFree() {
        return free;
    }

    public String getTeachingType() {
        return teachingType;
    }

    public int getMaximumLearnerCount() {
        return maximumLearnerCount;
    }

    public int getOtherLearnerCount() {
        return otherLearnerCount;
    }

    public int getCurrentLearnerCount() {
        return currentLearnerCount;
    }

    public int getTotalLearnerCount() {
        return otherLearnerCount + currentLearnerCount;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public int getAppliable() {
        return appliable;
    }

    public String getImage() {
        return image;
    }

    public String getImageUrl() {
        return ImageUtils.getImageUrl(image);
    }

    public String getSubscript() {
        return subscript;
    }

    public String getSuperscript() {
        return superscript;
    }

    public int getShowTotalLearnerCount() {
        return showTotalLearnerCount;
    }

    // - setters


    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setInstructorId(long instructorId) {
        this.instructorId = instructorId;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public void setTeachingType(String teachingType) {
        this.teachingType = teachingType;
    }

    public void setMaximumLearnerCount(int maximumLearnerCount) {
        this.maximumLearnerCount = maximumLearnerCount;
    }

    public void setCurrentLearnerCount(int currentLearnerCount) {
        this.currentLearnerCount = currentLearnerCount;
    }

    public void setOtherLearnerCount(int otherLearnerCount) {
        this.otherLearnerCount = otherLearnerCount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAppliable(int appliable) {
        this.appliable = appliable;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSubscript(String subscript) {
        this.subscript = subscript;
    }

    public void setSuperscript(String superscript) {
        this.superscript = superscript;
    }

    public void setShowTotalLearnerCount(int showTotalLearnerCount) {
        this.showTotalLearnerCount = showTotalLearnerCount;
    }
}
