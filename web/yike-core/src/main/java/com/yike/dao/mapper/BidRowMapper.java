package com.yike.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.yike.model.Bid;

/**
 * @author mixueqiang
 * @since Mar 15, 2014
 */
public class BidRowMapper implements RowMapper<Bid> {
  private static BidRowMapper instance;

  public static BidRowMapper getInstance() {
    if (instance == null) {
      instance = new BidRowMapper();
    }
    return instance;
  }

  @Override
  public Bid mapRow(ResultSet rs, int rowNum) throws SQLException {
    Bid entity = new Bid();
    entity.setId(rs.getLong("id"));
    entity.setProjectId(rs.getLong("projectId"));
    entity.setUserId(rs.getLong("userId"));
    entity.setWorkerId(rs.getLong("workerId"));
    entity.setCurrency(rs.getString("currency"));
    entity.setPrice(rs.getInt("price"));
    entity.setPeriod(rs.getInt("period"));
    entity.setMilestonePercentage(rs.getInt("milestonePercentage"));
    entity.setMilestone(rs.getInt("milestone"));
    entity.setProposal(rs.getString("proposal"));
    entity.setSampleResult(rs.getString("sampleResult"));
    entity.setAwardedBid(rs.getInt("awardedBid"));
    entity.setStatus(rs.getInt("status"));
    entity.setCreateTime(rs.getLong("createTime"));
    entity.setUpdateTime(rs.getLong("updateTime"));

    return entity;
  }

}
