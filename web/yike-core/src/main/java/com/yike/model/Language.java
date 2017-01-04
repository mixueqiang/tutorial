package com.yike.model;

import java.io.Serializable;

/**
 * @author mixueqiang
 * @since May 2, 2014
 */
public class Language implements Serializable {
  private static final long serialVersionUID = -1826352689357276361L;

  private String id;
  private String name;
  private String nameLocale;

  public Language() {
    super();
  }

  public Language(String id, String name, String nameLocale) {
    super();
    this.id = id;
    this.name = name;
    this.nameLocale = nameLocale;
  }

  public Language(Entity entity) {
    this(entity.getString("id"), entity.getString("name"), entity.getString("nameLocale"));
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameLocale() {
    return nameLocale;
  }

  public void setNameLocale(String nameLocale) {
    this.nameLocale = nameLocale;
  }

  @Override
  public String toString() {
    return "Language [id=" + id + ", name=" + name + ", nameLocale=" + nameLocale + "]";
  }

}
