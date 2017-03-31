package com.yike.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.yike.util.ImageUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.yike.model.Course;

/**
 * @author xueqiangmi
 * @since May 28, 2015
 */
public class CourseRowMapper implements RowMapper<Course> {
  private static CourseRowMapper instance;

  public static CourseRowMapper getInstance() {
    if (instance == null) {
      instance = new CourseRowMapper();
    }

    return instance;
  }

  @Override
  public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
    Course entity = new Course();

    entity.setId(rs.getLong(Course.SQL_ID));
    entity.setUserId(rs.getLong(Course.SQL_USER_ID));
    entity.setInstructorId(rs.getLong(Course.SQL_INSTRUCTOR_ID));
    entity.setCategoryId(rs.getLong(Course.SQL_CATEGORY_ID));

    entity.setName(rs.getString(Course.SQL_TITLE));
    entity.setFree(rs.getInt(Course.SQL_FREE));
    entity.setPrice(rs.getFloat(Course.SQL_PRICE));
    entity.setContent(rs.getString(Course.SQL_CONTENT));
    entity.setDescription(rs.getString(Course.SQL_DESCRIPTION));
    entity.setTeachingType(rs.getString(Course.SQL_TEACHING_TYPE));
    entity.setOnlineContactMethod(rs.getInt(Course.SQL_ONLINE_CONTACT_METHOD));
    entity.setAppliable(rs.getInt(Course.SQL_APPLIABLE));
    entity.setStatus(rs.getInt(Course.SQL_STATUS));
    entity.setCreateTime(rs.getLong(Course.SQL_CREATE_TIME));
    entity.setUpdateTime(rs.getLong(Course.SQL_UPDATE_TIME));

    entity.setCountMax(rs.getInt(Course.SQL_COUNT_MAX));
    entity.setCountThis(rs.getInt(Course.SQL_COUNT_THIS));
    entity.setCountOther(rs.getInt(Course.SQL_COUNT_OTHER));
    entity.setCountShow(rs.getBoolean(Course.SQL_COUNT_SHOW));

    entity.setSuperscript(rs.getString(Course.SQL_SUPERSCRIPT));
    entity.setSubscript(rs.getString(Course.SQL_SUBSCRIPT));
    String image = rs.getString(Course.SQL_IMAGE);

    if (StringUtils.isNotEmpty(image)) {
      entity.setImage(image);
      entity.getProperties().put("imageUrl", ImageUtils.getImageUrl(image));
    }

    String imageQrCode = rs.getString(Course.SQL_IMAGE_QR_CODE);
    if (StringUtils.isNotEmpty(imageQrCode)) {
      entity.setImageQrCode(imageQrCode);
    }

    return entity;
  }

}
