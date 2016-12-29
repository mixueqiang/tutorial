package com.yike.model;

import com.yike.model.BaseModel;

/**
 * @author ilakeyc
 * @since 26/12/2016
 */
public class Student extends BaseModel {

    private static final long serialVersionUID = -4739894062861502708L;
    private String nickname;
    private long applyId;
    private long courseId;
    private String phone;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getApplyId() {
        return applyId;
    }

    public void setApplyId(long applyId) {
        this.applyId = applyId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
