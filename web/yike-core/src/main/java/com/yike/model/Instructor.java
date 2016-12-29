package com.yike.model;

/**
 * @author ilakeyc
 * @since 19/12/2016
 */
public class Instructor extends BaseModel {
    private static final long serialVersionUID = -4756250692211037984L;

    public static final String SQL_TABLE_NAME = "instructor";
    public static final String SQL_ID = "id";
    public static final String SQL_NAME = "name";
    public static final String SQL_AVATAR = "avatar";
    public static final String SQL_PROFILE = "profile";
    public static final String SQL_USER_ID = "userId";
    public static final String SQL_CONTACTS = "contacts";
    public static final String SQL_CREATE_TIME = "createTime";
    public static final String SQL_UPDATE_TIME = "updateTime";
    public static final String SQL_STATUS = "status";


    private String avatar;
    private String profile;
    private long userId;
    private String contacts;


    public String getAvatar() {
        return avatar;
    }

    public String getProfile() {
        return profile;
    }

    public long getUserId() {
        return userId;
    }

    public String getContacts() {
        return contacts;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
}
