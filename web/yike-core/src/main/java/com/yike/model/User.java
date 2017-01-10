package com.yike.model;

import java.io.Serializable;

import com.yike.dao.EntityDao;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;

/**
 * @author mixueqiang
 * @since Mar 10, 2014
 */
public class User extends BaseModel implements Serializable {
  private static final long serialVersionUID = -8028956919647703012L;

  private String email;
  private String phone;
  private String password;
  private String username;
  private String firstName;
  private String lastName;
  private String avatar;
  private String gender;
  private String birthday;
  private String profile;
  private String locale;
  private String roles;
  private String securityCode;

  @Resource
  protected EntityDao entityDao;

  public String getAvatar() {
    return avatar;
  }

  public String getBirthday() {
    return birthday;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getGender() {
    return gender;
  }

  public String getLastName() {
    return lastName;
  }

  public String getLocale() {
    return locale;
  }

  public String getPassword() {
    return password;
  }

  public String getPhone() {
    return phone;
  }

  public String getProfile() {
    return profile;
  }

  public String getRoles() {
    return roles;
  }

  public String getSecurityCode() {
    return securityCode;
  }

  public String getUsername() {
    return username;
  }

  // TODO 修改isAdmin
  public boolean isAdmin() {
    return StringUtils.endsWith(email, "@transkip.com");
  }

  public boolean isInstructor() {return entityDao.exists(Instructor.SQL_TABLE_NAME, Instructor.SQL_USER_ID, this.getId());}

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public void setRoles(String roles) {
    this.roles = roles;
  }

  public void setSecurityCode(String securityCode) {
    this.securityCode = securityCode;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return "User [email=" + email + ", username=" + username + ", id=" + id + "]";
  }

}
