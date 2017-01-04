package com.yike.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yike.dao.BaseDao;
import com.yike.util.CalendarUtils;
import com.yike.dao.mapper.BidRowMapper;
import com.yike.model.Bid;

/**
 * @author mixueqiang
 * @since Mar 15, 2014
 */
@Repository
public class BidDao extends BaseDao {

  public Bid get(long id) {
    String sql = "SELECT * FROM bid WHERE id = ?";
    return queryForNullable(sql, new Object[] { id }, BidRowMapper.getInstance());
  }

  public Bid getChosenBid(long projectId) {
    String sql = "SELECT * FROM bid WHERE projectId = ? AND status = 1";
    return queryForNullable(sql, new Object[] { projectId }, BidRowMapper.getInstance());
  }

  public List<Bid> getBidded(long userId, int page, int size) {
    String sql = "SELECT * FROM bid WHERE userId = ? AND status = 1 ORDER BY id DESC LIMIT ? OFFSET ?";
    return jdbcTemplate.query(sql, new Object[] { userId, size, (page - 1) * size }, BidRowMapper.getInstance());
  }

  public void updateStatus(long id, int status) {
    String sql = "UPDATE bid SET status = ?, updateTime = ? WHERE id = ?";
    jdbcTemplate.update(sql, new Object[] { status, CalendarUtils.now(), id });
  }

}
