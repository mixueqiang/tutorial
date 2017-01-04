package com.yike.model;

import java.io.Serializable;

import com.yike.model.BaseModel;
import com.yike.model.User;

/**
 * @author mixueqiang
 * @since Mar 15, 2014
 */
public class Bid extends BaseModel implements Serializable {
  private static final long serialVersionUID = -4924707201645679385L;

  private long projectId;
  private long userId;
  private long workerId;
  private String currency;
  private int price;
  private int period;
  private int milestonePercentage;
  private int milestone;
  private String proposal;
  private String sampleResult;
  private int awardedBid;
  private int status;

  private User user;
  private Project project;

  public long getProjectId() {
    return projectId;
  }

  public void setProjectId(long projectId) {
    this.projectId = projectId;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getWorkerId() {
    return workerId;
  }

  public void setWorkerId(long workerId) {
    this.workerId = workerId;
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

  public int getPeriod() {
    return period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public int getMilestonePercentage() {
    return milestonePercentage;
  }

  public void setMilestonePercentage(int milestonePercentage) {
    this.milestonePercentage = milestonePercentage;
  }

  public int getMilestone() {
    return milestone;
  }

  public void setMilestone(int milestone) {
    this.milestone = milestone;
  }

  public String getProposal() {
    return proposal;
  }

  public void setProposal(String proposal) {
    this.proposal = proposal;
  }

  public String getSampleResult() {
    return sampleResult;
  }

  public void setSampleResult(String sampleResult) {
    this.sampleResult = sampleResult;
  }

  public int getAwardedBid() {
    return awardedBid;
  }

  public void setAwardedBid(int awardedBid) {
    this.awardedBid = awardedBid;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

}
