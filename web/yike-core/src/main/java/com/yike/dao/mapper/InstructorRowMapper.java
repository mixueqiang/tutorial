package com.yike.dao.mapper;

import com.yike.model.Instructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ilakeyc
 * @since 19/12/2016
 */
public class InstructorRowMapper implements RowMapper<Instructor> {
    private static InstructorRowMapper instance;

    public static InstructorRowMapper getInstance() {
        if (null == instance) {
            instance = new InstructorRowMapper();
        }
        return instance;
    }

    @Override
    public Instructor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Instructor entity = new Instructor();

        entity.setId(rs.getLong(Instructor.SQL_ID));
        entity.setName(rs.getString(Instructor.SQL_NAME));
        entity.setAvatar(rs.getString(Instructor.SQL_AVATAR));
        entity.setProfile(rs.getString(Instructor.SQL_PROFILE));
        entity.setUserId(rs.getLong(Instructor.SQL_USER_ID));
        entity.setStatus(rs.getInt(Instructor.SQL_STATUS));
        entity.setContacts(rs.getString(Instructor.SQL_CONTACTS));
        entity.setUpdateTime(rs.getLong(Instructor.SQL_UPDATE_TIME));
        entity.setCreateTime(rs.getLong(Instructor.SQL_CREATE_TIME));

        return entity;
    }

}
