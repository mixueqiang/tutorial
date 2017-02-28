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
  private String content;
  private String url;
  private String contact;
  private String wechat;
  private String qqGroup;

  public String getContact() {
    return contact;
  }

  public String getContent() {
    return content;
  }

  public String getQqGroup() {
    return qqGroup;
  }

  public long getSkillId() {
    return skillId;
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

  public void setQqGroup(String qqGroup) {
    this.qqGroup = qqGroup;
  }

  public void setSkillId(long skillId) {
    this.skillId = skillId;
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
