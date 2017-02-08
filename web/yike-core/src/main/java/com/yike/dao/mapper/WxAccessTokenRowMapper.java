package com.yike.dao.mapper;


import com.yike.model.WxAccessToken;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ilakeyc
 * @since 08/02/2017
 */
public class WxAccessTokenRowMapper implements RowMapper<WxAccessToken> {
  private static WxAccessTokenRowMapper instance;

  public static WxAccessTokenRowMapper getInstance() {
    if (instance == null) {
      instance = new WxAccessTokenRowMapper();
    }
    return instance;
  }

  @Override
  public WxAccessToken mapRow(ResultSet rs, int rowNum) throws SQLException {

    WxAccessToken entity = new WxAccessToken();

    entity.setId(rs.getLong("id"));
    entity.setAccess_token(rs.getString("access_token"));
    entity.setExpires_in(rs.getLong("expires_in"));
    entity.setCreateTime(rs.getLong("createTime"));
    entity.setUpdateTime(rs.getLong("updateTime"));
    entity.setStatus(rs.getInt("status"));

    return null;
  }
}
