package com.yike.dao.mapper;


import com.yike.model.WXAccessToken;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ilakeyc
 * @since 08/02/2017
 */
public class WXAccessTokenRowMapper implements RowMapper<WXAccessToken> {
  private static WXAccessTokenRowMapper instance;

  public static WXAccessTokenRowMapper getInstance() {
    if (instance == null) {
      instance = new WXAccessTokenRowMapper();
    }
    return instance;
  }

  @Override
  public WXAccessToken mapRow(ResultSet rs, int rowNum) throws SQLException {

    WXAccessToken entity = new WXAccessToken();

    entity.setId(rs.getLong("id"));
    entity.setAccess_token(rs.getString("access_token"));
    entity.setExpires_in(rs.getLong("expires_in"));
    entity.setCreateTime(rs.getLong("createTime"));
    entity.setUpdateTime(rs.getLong("updateTime"));
    entity.setStatus(rs.getInt("status"));

    return null;
  }
}
