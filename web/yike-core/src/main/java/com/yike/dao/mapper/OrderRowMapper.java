package com.yike.dao.mapper;

import com.yike.model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mixueqiang
 * @since Mar 16, 2014
 */
public class OrderRowMapper implements RowMapper<Order> {
  private static OrderRowMapper instance;

  public static OrderRowMapper getInstance() {
    if (instance == null) {
      instance = new OrderRowMapper();
    }

    return instance;
  }

  @Override
  public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
    Order entity = new Order();
    entity.setId(rs.getLong("id"));
    entity.setOrderId(rs.getString("orderId"));
    entity.setBuyerId(rs.getLong("buyerId"));
    entity.setSellerId(rs.getLong("sellerId"));
    entity.setProjectId(rs.getLong("projectId"));
    entity.setBidId(rs.getLong("bidId"));
    entity.setType(rs.getInt("type"));
    entity.setCurrency(rs.getString("currency"));
    entity.setPrice(rs.getInt("price"));
    entity.setProfile(rs.getString("profile"));
    entity.setStatus(rs.getInt("status"));
    entity.setPaypalTxnId(rs.getString("paypalTxnId"));
    entity.setCreateTime(rs.getLong("createTime"));
    entity.setUpdateTime(rs.getLong("updateTime"));

    return entity;
  }

}
