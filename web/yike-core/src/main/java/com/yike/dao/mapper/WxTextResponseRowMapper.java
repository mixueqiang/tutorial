package com.yike.dao.mapper;

import com.yike.model.WxTextResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ilakeyc
 * @since 2017/3/17
 */
public class WxTextResponseRowMapper implements RowMapper<WxTextResponse> {

    private static WxTextResponseRowMapper instance;

    public static WxTextResponseRowMapper getInstance() {
        if (instance == null) {
            instance = new WxTextResponseRowMapper();
        }
        return instance;
    }

    @Override
    public WxTextResponse mapRow(ResultSet rs, int rowNum) throws SQLException {

        WxTextResponse entity = new WxTextResponse();

        entity.setResult(rs.getString("result"));
        entity.setTarget(rs.getString("target"));
        entity.setStatus(rs.getInt("status"));
        entity.setCreateTime(rs.getLong("createTime"));
        entity.setUpdateTime(rs.getLong("updateTime"));
        entity.setId(rs.getLong("id"));

        return entity;
    }

}
