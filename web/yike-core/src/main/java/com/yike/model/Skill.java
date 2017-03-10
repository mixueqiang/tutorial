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
  private long skillCount;
  private long resourceCount;
  private long articleCount;
  private long userCount;
  private long companyCount;

  public long getArticleCount() {
    return articleCount;
  }

  public long getCompanyCount() {
    return companyCount;
  }

  public String getDescription() {
    return description;
  }

  public String getImage() {
    return image;
  }

  public long getResourceCount() {
    return resourceCount;
  }

  public long getSkillCount() {
    return skillCount;
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

  public void setArticleCount(long articleCount) {
    this.articleCount = articleCount;
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

  public void setResourceCount(long resourceCount) {
    this.resourceCount = resourceCount;
  }

  public void setSkillCount(long skillCount) {
    this.skillCount = skillCount;
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
