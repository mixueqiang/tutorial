package com.yike.dao.mapper;

import com.yike.model.WxUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ilakeyc
 * @since 2017/2/15
 */
public class WxUserRowMapper implements RowMapper<WxUser> {
  private static WxUserRowMapper instance;

  public static WxUserRowMapper getInstance() {
    if (instance == null) {
      instance = new WxUserRowMapper();
    }

    return instance;
  }

  @Override
  public WxUser mapRow(ResultSet rs, int rowNum) throws SQLException {
    WxUser entity = new WxUser();

    entity.setId(rs.getLong("id"));
    entity.setUserId(rs.getLong("userId"));
    entity.setSubscribe(rs.getInt("subscribe"));
    entity.setOpenid(rs.getString("openId"));
    entity.setNickname(rs.getString("nickName"));
    entity.setSex(rs.getInt("sex"));
    entity.setLanguage(rs.getString("language"));
    entity.setCity(rs.getString("city"));
    entity.setProvince(rs.getString("province"));
    entity.setCountry(rs.getString("country"));
    entity.setHeadimgurl(rs.getString("headImgUrl"));
    entity.setSubscribe_time(rs.getLong("subscribeTime"));
    entity.setUnionid(rs.getString("unionid"));
    entity.setRemark(rs.getString("remark"));
    entity.setGroupid(rs.getString("groupid"));
    entity.setCreateTime(rs.getLong("createTime"));
    entity.setUpdateTime(rs.getLong("updateTime"));
    entity.setInviterId(rs.getString("inviterId"));
    entity.setIsStudent(rs.getInt("isStudent"));
    entity.setQrTicket(rs.getString("qrTicket"));
    entity.setStatus(rs.getInt("status"));

    return entity;
  }
}
