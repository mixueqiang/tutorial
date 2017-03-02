package com.yike.dao.mapper;

import com.yike.model.CourseSchedule;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ilakeyc
 * @since 2017/3/2
 */
public class CourseScheduleRowMapper implements RowMapper<CourseSchedule> {

    private static CourseScheduleRowMapper instance;

    public static CourseScheduleRowMapper getInstance() {
        if (instance == null) {
            instance = new CourseScheduleRowMapper();
        }
        return instance;
    }

    @Override
    public CourseSchedule mapRow(ResultSet rs, int rowNum) throws SQLException {

        CourseSchedule entity = new CourseSchedule();

        entity.setId(rs.getLong("id"));
        entity.setCourseId(rs.getLong("courseId"));
        entity.setLaunchDate(rs.getString("launchDate"));
        entity.setLaunchTime(rs.getString("launchTime"));
        entity.setCreateTime(rs.getLong("createTime"));
        entity.setUpdateTime(rs.getLong("updateTime"));

        return entity;
    }
}
