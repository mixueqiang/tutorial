package com.yike.model;

import java.io.Serializable;


/**
 * @author mixueqiang
 * @since Mar 16, 2014
 */
public class Order extends BaseModel implements Serializable {
  private static final long serialVersionUID = -7800751131459581236L;

  private String orderId;
  private long buyerId;
  private long sellerId;
  private long projectId;
  private long bidId;
  private int type;
  private String currency;
  private int price;
  private String profile;
  private int status;
  private String paypalTxnId;

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public long getBuyerId() {
    return buyerId;
  }

  public void setBuyerId(long buyerId) {
    this.buyerId = buyerId;
  }

  public long getSellerId() {
    return sellerId;
  }

  public void setSellerId(long sellerId) {
    this.sellerId = sellerId;
  }

  public long getProjectId() {
    return projectId;
  }

  public void setProjectId(long projectId) {
    this.projectId = projectId;
  }

  public long getBidId() {
    return bidId;
  }

  public void setBidId(long bidId) {
    this.bidId = bidId;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getPaypalTxnId() {
    return paypalTxnId;
  }

  public void setPaypalTxnId(String paypalTxnId) {
    this.paypalTxnId = paypalTxnId;
  }

  @Override
  public String toString() {
    return "Order [orderId=" + orderId + ", buyerId=" + buyerId + ", sellerId=" + sellerId + ", projectId=" + projectId
            + ", bidId=" + bidId + ", type=" + type + ", currency=" + currency + ", price=" + price + ", status=" + status
            + ", paypalTxnId=" + paypalTxnId + "]";
  }

}
