package com.yike.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.yike.model.Category;

/**
 * @author mixueqiang
 * @since Apr 29, 2016
 */
public class CategoryRowMapper implements RowMapper<Category> {
  private static CategoryRowMapper instance;

  public static CategoryRowMapper getInstance() {
    if (instance == null) {
      instance = new CategoryRowMapper();
    }

    return instance;
  }

  @Override
  public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
    Category entity = new Category();
    entity.setId(rs.getLong(Category.SQL_ID));
    entity.setParentId(rs.getLong(Category.SQL_PARENT_ID));
    entity.setSlug(rs.getString(Category.SQL_SLUG));
    entity.setName(rs.getString(Category.SQL_NAME));
    entity.setImage(rs.getString(Category.SQL_IMAGE));
    entity.setStatus(rs.getInt(Category.SQL_STATUS));
    entity.setCreateTime(rs.getLong(Category.SQL_CREATE_TIME));
    entity.setUpdateTime(rs.getLong(Category.SQL_UPDATE_TIME));

    return entity;
  }

}
