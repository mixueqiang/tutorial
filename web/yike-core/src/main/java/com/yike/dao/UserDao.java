package com.yike.dao;

import org.springframework.stereotype.Repository;

import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.User;

/**
 * @author mixueqiang
 * @since Mar 11, 2014
 */
@Repository
public class UserDao extends EntityDao {

  public User get(long id) {
    String sql = "SELECT * FROM user WHERE id = ?";
    return queryForNullable(sql, new Object[] { id }, UserRowMapper.getInstance());
  }

  public User getByUsername(String username) {
    String sql = "SELECT * FROM user WHERE username = ?";
    return queryForNullable(sql, new Object[] { username }, UserRowMapper.getInstance());
  }

  public void update(User user) {
    String sql = "UPDATE user SET status = ?, updateTime = ? WHERE id = ?";
    jdbcTemplate.update(sql, new Object[] { user.getStatus(), user.getUpdateTime(), user.getId() });
  }

}
