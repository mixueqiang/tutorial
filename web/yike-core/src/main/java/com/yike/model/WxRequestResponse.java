package com.yike.model;

/**
 * @author ilakeyc
 * @since 2017/2/13
 */
public class WxRequestResponse<T> {

  private String errorCode;
  private String errorMessage;
  private T object;

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public T getObject() {
    return object;
  }

  public void setObject(T object) {
    this.object = object;
  }
}
