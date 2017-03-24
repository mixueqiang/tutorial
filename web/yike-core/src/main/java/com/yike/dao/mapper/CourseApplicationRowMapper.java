package com.yike.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.yike.model.CourseApplication;

/**
 * @author ilakeyc
 * @since 14/12/2016
 */
public class CourseApplicationRowMapper implements RowMapper<CourseApplication> {
  private static CourseApplicationRowMapper instance;

  public static CourseApplicationRowMapper getInstance() {
    if (null == instance) {
      instance = new CourseApplicationRowMapper();
    }
    return instance;
  }

  @Override
  public CourseApplication mapRow(ResultSet rs, int rowNum) throws SQLException {
    CourseApplication entity = new CourseApplication();

    entity.setId(rs.getLong(CourseApplication.SQL_ID));
    entity.setUserId(rs.getLong(CourseApplication.SQL_USER_ID));
    entity.setCourseId(rs.getLong(CourseApplication.SQL_COURSE_ID));
    entity.setName(rs.getString(CourseApplication.SQL_NAME));
    entity.setPhone(rs.getString(CourseApplication.SQL_PHONE));
    entity.setQq(rs.getString(CourseApplication.SQL_QQ));
    entity.setWechat(rs.getString(CourseApplication.SQL_WECHAT));
    entity.setPrice(rs.getFloat(CourseApplication.SQL_PRICE));
    entity.setOrderId(rs.getString(CourseApplication.SQL_ORDER_ID));
    entity.setProgress(rs.getInt(CourseApplication.SQL_PROGRESS));
    entity.setStatus(rs.getInt(CourseApplication.SQL_STATUS));
    entity.setCreateTime(rs.getLong(CourseApplication.SQL_CREATE_TIME));
    entity.setUpdateTime(rs.getLong(CourseApplication.SQL_UPDATE_TIME));

    return entity;
  }

}
