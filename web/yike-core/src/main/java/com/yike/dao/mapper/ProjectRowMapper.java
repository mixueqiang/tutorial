package com.yike.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.yike.model.Project;

/**
 * @author mixueqiang
 * @since Mar 9, 2014
 */
public class ProjectRowMapper implements RowMapper<Project> {
  private static ProjectRowMapper instance;

  public static ProjectRowMapper getInstance() {
    if (instance == null) {
      instance = new ProjectRowMapper();
    }

    return instance;
  }

  @Override
  public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
    Project entity = new Project();
    entity.setId(rs.getLong("id"));
    entity.setUserId(rs.getLong("userId"));
    entity.setSourceLanguageId(rs.getString("sourceLanguageId"));
    entity.setTargetLanguageId(rs.getString("targetLanguageId"));
    entity.setName(rs.getString("name"));
    entity.setDescription(rs.getString("description"));
    entity.setSample(rs.getString("sample"));
    entity.setStart(rs.getString("start"));
    entity.setEnd(rs.getString("end"));
    entity.setBudgetType(rs.getInt("budgetType"));
    entity.setCurrency(rs.getString("currency"));
    entity.setBudgetMin(rs.getInt("budgetMin"));
    entity.setBudgetMax(rs.getInt("budgetMax"));
    entity.setBids(rs.getInt("bids"));
    entity.setAverageBid(rs.getInt("averageBid"));
    entity.setStatus(rs.getInt("status"));
    entity.setCreateTime(rs.getLong("createTime"));
    entity.setUpdateTime(rs.getLong("updateTime"));

    return entity;
  }

}
