package com.yike.model;

/**
 * @author mixueqiang
 * @since Feb 28, 2017
 *
 */
public class Resource extends BaseModel {
  private static final long serialVersionUID = 1453542833608050366L;

  private long skillId;
  private long userId;
  private String title;
  private String content;
  private String url;
  private String password;
  private String contact;
  private String wechat;
  private String qqGroup;

  public String getContact() {
    return contact;
  }

  public String getContent() {
    return content;
  }

  public String getPassword() {
    return password;
  }

  public String getQqGroup() {
    return qqGroup;
  }

  public long getSkillId() {
    return skillId;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public long getUserId() {
    return userId;
  }

  public String getWechat() {
    return wechat;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setQqGroup(String qqGroup) {
    this.qqGroup = qqGroup;
  }

  public void setSkillId(long skillId) {
    this.skillId = skillId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public void setWechat(String wechat) {
    this.wechat = wechat;
  }

}
