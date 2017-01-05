package com.yike.model;

/**
 * @author ilakeyc
 * @since 05/01/2017
 */
public class Invitation extends BaseModel {
  private static final long serialVersionUID = -3753196547733484909L;

  public static final String SQL_TABLE_NAME = "invitation";
  public static final String SQL_ID = "id";
  public static final String SQL_NAME = "name";
  public static final String SQL_CONTACTS = "contacts";
  public static final String SQL_CONTENT = "content";
  public static final String SQL_STATUS = "status";
  public static final String SQL_CREATE_TIME = "createTime";
  public static final String SQL_UPDATE_TIME = "updateTime";


  private String contacts;
  private String content;

  public String getContacts() {
    return contacts;
  }

  public void setContacts(String contacts) {
    this.contacts = contacts;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
