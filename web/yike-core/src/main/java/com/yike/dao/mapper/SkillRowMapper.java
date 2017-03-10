package com.yike.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.yike.model.Skill;

/**
 * @author mixueqiang
 * @since Jan 17, 2017
 *
 */
public class SkillRowMapper implements RowMapper<Skill> {
  private static SkillRowMapper instance;

  public static SkillRowMapper getInstance() {
    if (instance == null) {
      instance = new SkillRowMapper();
    }

    return instance;
  }

  @Override
  public Skill mapRow(ResultSet rs, int rowNum) throws SQLException {
    Skill entity = new Skill();

    entity.setId(rs.getLong("id"));
    entity.setUserId(rs.getLong("userId"));
    entity.setSlug(rs.getString("slug"));
    entity.setName(rs.getString("name"));
    entity.setImage(rs.getString("image"));
    entity.setDescription(rs.getString("description"));
    entity.setUserCount(rs.getLong("skillCount"));
    entity.setResourceCount(rs.getLong("resourceCount"));
    entity.setCompanyCount(rs.getLong("articleCount"));
    entity.setUserCount(rs.getLong("userCount"));
    entity.setCompanyCount(rs.getLong("companyCount"));
    entity.setStatus(rs.getInt("status"));
    entity.setCreateTime(rs.getLong("createTime"));
    entity.setUpdateTime(rs.getLong("updateTime"));

    return entity;
  }

}
