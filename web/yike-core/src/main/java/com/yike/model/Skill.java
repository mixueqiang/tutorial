package com.yike.model;

/**
 * @author mixueqiang
 * @since Jan 17, 2017
 *
 */
public class Skill extends BaseModel {
  private static final long serialVersionUID = -3853776778811837666L;

  private long userId;
  private String slug;
  private String image;
  private String description;
  private long userCount;
  private long companyCount;

  public long getCompanyCount() {
    return companyCount;
  }

  public String getDescription() {
    return description;
  }

  public String getImage() {
    return image;
  }

  public String getSlug() {
    return slug;
  }

  public long getUserCount() {
    return userCount;
  }

  public long getUserId() {
    return userId;
  }

  public void setCompanyCount(long companyCount) {
    this.companyCount = companyCount;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public void setUserCount(long userCount) {
    this.userCount = userCount;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

}
