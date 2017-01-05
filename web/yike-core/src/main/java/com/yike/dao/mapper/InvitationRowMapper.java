package com.yike.dao.mapper;

import com.yike.model.Invitation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ilakeyc
 * @since 05/01/2017
 */
public class InvitationRowMapper implements RowMapper<Invitation> {
  public static InvitationRowMapper instance;

  public static InvitationRowMapper getInstance() {
    if (null == instance) {
      instance = new InvitationRowMapper();
    }
    return instance;
  }

  @Override
  public Invitation mapRow(ResultSet rs, int rowNumber) throws
          SQLException {
    Invitation entity = new Invitation();

    entity.setId(rs.getLong(Invitation.SQL_ID));
    entity.setName(rs.getString(Invitation.SQL_NAME));
    entity.setContacts(rs.getString(Invitation.SQL_CONTACTS));
    entity.setContent(rs.getString(Invitation.SQL_CONTENT));
    entity.setUpdateTime(rs.getLong(Invitation.SQL_UPDATE_TIME));
    entity.setCreateTime(rs.getLong(Invitation.SQL_CREATE_TIME));
    entity.setStatus(rs.getInt(Invitation.SQL_STATUS));

    return entity;
  }
}
