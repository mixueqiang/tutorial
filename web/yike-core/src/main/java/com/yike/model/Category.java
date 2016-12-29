package com.yike.model;

import java.io.Serializable;

/**
 * @author mixueqiang
 * @since Apr 29, 2016
 */
public class Category extends BaseModel implements Serializable {
  private static final long serialVersionUID = 5472992695418748843L;

  public static final String SQL_TABLE_NAME = "category";
  public static final String SQL_ID = "id";
  public static final String SQL_PARENT_ID = "parentId";
  public static final String SQL_NAME = "name";
  public static final String SQL_SLUG = "slug";
  public static final String SQL_IMAGE = "image";
  public static final String SQL_STATUS = "status";
  public static final String SQL_CREATE_TIME = "createTime";
  public static final String SQL_UPDATE_TIME = "updateTime";


  private String image;
  private long parentId;
  private String slug;

  public String getImage() {
    return image;
  }

  public long getParentId() {
    return parentId;
  }

  public String getSlug() {
    return slug;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setParentId(long parentId) {
    this.parentId = parentId;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

}
