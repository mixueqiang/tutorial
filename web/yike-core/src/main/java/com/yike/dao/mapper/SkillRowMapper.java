package com.yike.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.yike.model.Course;
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
    entity.setDescription(rs.getString("description"));
    entity.setStatus(rs.getInt(Course.SQL_STATUS));
    entity.setUserCount(rs.getLong("userCount"));
    entity.setCompanyCount(rs.getLong("companyCount"));
    entity.setCreateTime(rs.getLong("createTime"));
    entity.setUpdateTime(rs.getLong("updateTime"));

    return entity;
  }

}
