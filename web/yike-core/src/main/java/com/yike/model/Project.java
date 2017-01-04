package com.yike.model;

import java.io.Serializable;

import com.yike.model.BaseModel;
import com.yike.model.Language;
import com.yike.model.User;

/**
 * @author mixueqiang
 * @since Mar 8, 2014
 */
public class Project extends BaseModel implements Serializable {
  private static final long serialVersionUID = 8914410303675626636L;

  private long userId;
  private String sourceLanguageId;
  private String targetLanguageId;
  private String description;
  private String sample;
  private String start;
  private String end;
  private int budgetType;
  private String currency;
  private int budgetMin;
  private int budgetMax;
  private int bids;
  private int averageBid;
  private int status;
  private long bidEndTime;

  private Language sourceLanguage;
  private Language targetLanguage;
  private User user;
  private Bid bid;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getSourceLanguageId() {
    return sourceLanguageId;
  }

  public void setSourceLanguageId(String sourceLanguageId) {
    this.sourceLanguageId = sourceLanguageId;
  }

  public String getTargetLanguageId() {
    return targetLanguageId;
  }

  public void setTargetLanguageId(String targetLanguageId) {
    this.targetLanguageId = targetLanguageId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSample() {
    return sample;
  }

  public void setSample(String sample) {
    this.sample = sample;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public int getBudgetType() {
    return budgetType;
  }

  public void setBudgetType(int budgetType) {
    this.budgetType = budgetType;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public int getBudgetMin() {
    return budgetMin;
  }

  public void setBudgetMin(int budgetMin) {
    this.budgetMin = budgetMin;
  }

  public int getBudgetMax() {
    return budgetMax;
  }

  public void setBudgetMax(int budgetMax) {
    this.budgetMax = budgetMax;
  }

  public int getBids() {
    return bids;
  }

  public void setBids(int bids) {
    this.bids = bids;
  }

  public int getAverageBid() {
    return averageBid;
  }

  public void setAverageBid(int averageBid) {
    this.averageBid = averageBid;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public long getBidEndTime() {
    return bidEndTime;
  }

  public void setBidEndTime(long bidEndTime) {
    this.bidEndTime = bidEndTime;
  }

  public Language getSourceLanguage() {
    return sourceLanguage;
  }

  public void setSourceLanguage(Language sourceLanguage) {
    this.sourceLanguage = sourceLanguage;
  }

  public Language getTargetLanguage() {
    return targetLanguage;
  }

  public void setTargetLanguage(Language targetLanguage) {
    this.targetLanguage = targetLanguage;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Bid getBid() {
    return bid;
  }

  public void setBid(Bid bid) {
    this.bid = bid;
  }

}
