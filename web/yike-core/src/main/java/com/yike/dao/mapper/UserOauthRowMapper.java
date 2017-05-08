package com.yike.dao.mapper;

import com.yike.model.UserOauth;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ilakeyc
 * @since 2017/3/28
 */
public class UserOauthRowMapper implements RowMapper<UserOauth> {
    private static UserOauthRowMapper instance;

    public static UserOauthRowMapper getInstance() {
        if (instance == null) {
            instance = new UserOauthRowMapper();
        }
        return instance;
    }

    @Override
    public UserOauth mapRow(ResultSet rs, int rowNum) throws SQLException {

        UserOauth entity = new UserOauth();

        entity.setId(rs.getLong("id"));
        entity.setUserId(rs.getLong("userId"));
        entity.setOpenId(rs.getString("openId"));
        entity.setUnionId(rs.getString("unionId"));
        entity.setProvider(rs.getString("provider"));
        entity.setStatus(rs.getInt("status"));
        entity.setCreateTime(rs.getLong("createTime"));
        entity.setUpdateTime(rs.getLong("updateTime"));

        return entity;
    }
}
