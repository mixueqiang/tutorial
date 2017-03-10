package com.yike.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.yike.model.Resource;

/**
 * @author mixueqiang
 * @since Feb 28, 2017
 *
 */
public class ResourceRowMapper implements RowMapper<Resource> {
  private static ResourceRowMapper instance;

  public static ResourceRowMapper getInstance() {
    if (instance == null) {
      instance = new ResourceRowMapper();
    }

    return instance;
  }

  @Override
  public Resource mapRow(ResultSet rs, int rowNum) throws SQLException {
    Resource entity = new Resource();

    entity.setId(rs.getLong("id"));
    entity.setSkillId(rs.getLong("skillId"));
    entity.setUserId(rs.getLong("userId"));
    entity.setTitle(rs.getString("title"));
    entity.setContent(rs.getString("content"));
    entity.setContact(rs.getString("contact"));
    entity.setQqGroup(rs.getString("qqGroup"));
    entity.setWechat(rs.getString("wechat"));
    entity.setStatus(rs.getInt("status"));
    entity.setCreateTime(rs.getLong("createTime"));
    entity.setUpdateTime(rs.getLong("updateTime"));

    return entity;
  }

}
