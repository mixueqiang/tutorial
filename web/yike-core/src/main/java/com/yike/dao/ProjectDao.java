package com.yike.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.yike.util.CalendarUtils;
import com.yike.util.Pair;
import com.yike.dao.mapper.ProjectRowMapper;
import com.yike.model.Project;

/**
 * @author mixueqiang
 * @since Mar 9, 2014
 */
@Repository
public class ProjectDao extends EntityDao {

  public Project get(long id) {
    String sql = "SELECT * FROM project WHERE id = ?";
    return queryForNullable(sql, new Object[] { id }, ProjectRowMapper.getInstance());
  }

  public List<Project> getByPagination(int page, int size) {
    String sql = "SELECT * FROM project WHERE status > 0 ORDER BY id DESC LIMIT ? OFFSET ?";
    return jdbcTemplate.query(sql, new Object[] { size, (page - 1) * size }, ProjectRowMapper.getInstance());
  }

  public List<Project> getByUser(long userId, int page, int size) {
    String sql = "SELECT * FROM project WHERE userId = ? ORDER BY id DESC LIMIT ? OFFSET ?";
    return jdbcTemplate.query(sql, new Object[] { userId, size, (page - 1) * size }, ProjectRowMapper.getInstance());
  }

  public List<Project> getLatest() {
    // TODO Auto-generated method stub
    return null;
  }

  public Pair<Integer, List<Project>> query(int page) {
    return query(page, PAGE_SIZE_MEDIUM);
  }

  public Pair<Integer, List<Project>> query(int page, int size) {
    return findAndCount("project", page, size, ProjectRowMapper.getInstance(), ORDER_OPTION_DESC);
  }

  public Project save(Project project) {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("project")
        .usingGeneratedKeyColumns("id");

    Map<String, Object> args = new HashMap<String, Object>();
    args.put("userId", project.getUserId());
    args.put("name", project.getName());
    args.put("description", project.getDescription());
    args.put("createTime", project.getCreateTime());
    args.put("updateTime", project.getCreateTime());

    Number id = simpleJdbcInsert.executeAndReturnKey(args);
    project.setId(id.longValue());
    return project;
  }

  public void updateBidInfo(long id, int bids, String averageBid) {
    String sql = "UPDATE project SET bids = ?, averageBid = ? WHERE id = ?";
    jdbcTemplate.update(sql, new Object[] { bids, averageBid, id });
  }

  public void updateStatus(long id, int status) {
    String sql = "UPDATE project SET status = ?, updateTime = ? WHERE id = ?";
    jdbcTemplate.update(sql, new Object[] { status, CalendarUtils.now(), id });
  }

}
