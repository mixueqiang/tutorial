package com.yike.model;

/**
 * @author ilakeyc
 * @since 2017/3/2
 */
public class CourseSchedule extends BaseModel {

    private static final long serialVersionUID = 6998743849045989413L;

    private long courseId;
    private String launchDate;
    private String launchTime;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }
}
