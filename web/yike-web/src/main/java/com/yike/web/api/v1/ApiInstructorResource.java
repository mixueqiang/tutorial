package com.yike.web.api.v1;

import com.yike.Constants;
import com.yike.dao.mapper.InstructorRowMapper;
import com.yike.model.Entity;
import com.yike.model.User;
import com.yike.util.ResponseBuilder;
import com.yike.model.Instructor;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 19/12/2016
 */

@Path("/api/v1/instructor")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiInstructorResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiInstructorResource.class);

  @POST
  @Path("update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> update(
          @FormParam("name") String name,
          @FormParam("avatar") String avatar,
          @FormParam("profile") String profile,
          @FormParam("contacts") String contacts) {
    User user = getSessionUser();
    if (null == user) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }
    // 检查表单
    Map<String, Object> checkResult = checkParameters(
            name,
            avatar,
            profile,
            contacts);
    if ((Integer) checkResult.get("e") != 0) {
      return checkResult;
    }

    // 查找讲师
    Map<String, Object> instructorFindCondition = new HashMap<String, Object>();
    instructorFindCondition.put(Instructor.SQL_USER_ID, user.getId());
    instructorFindCondition.put(Instructor.SQL_STATUS, Constants.STATUS_OK);
    Instructor instructor = entityDao.findOne(
            Instructor.SQL_TABLE_NAME,
            instructorFindCondition,
            InstructorRowMapper.getInstance());
    if (null == instructor) {
      return ResponseBuilder.error(13404, "该讲师不存在。");
    }

    long instructorId = instructor.getId();

    Map<String, Object> instructorUpdateValues = new HashMap<String, Object>();
    instructorUpdateValues.put(Instructor.SQL_NAME, name);
    instructorUpdateValues.put(Instructor.SQL_AVATAR, avatar);
    instructorUpdateValues.put(Instructor.SQL_PROFILE, profile);
    instructorUpdateValues.put(Instructor.SQL_CONTACTS, contacts);

    try {
      // 更新信息
      entityDao.update(
              Instructor.SQL_TABLE_NAME,
              Instructor.SQL_ID,
              instructorId,
              instructorUpdateValues);
      return ResponseBuilder.OK;
    } catch (Throwable t) {
      LOG.error("unable to update an instructor profile", t);
      return ResponseBuilder.error(50000, "更新资料失败。");
    }
  }

  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> save(
          @FormParam("name") String name,
          @FormParam("avatar") String avatar,
          @FormParam("profile") String profile,
          @FormParam("contacts") String contacts) {
    // 判断条件
    User user = getSessionUser();
    if (null == user) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    Map<String, Object> checkResult = checkParameters(
            name,
            avatar,
            profile,
            contacts);

    if ((Integer) checkResult.get("e") != 0) {
      return checkResult;
    }

    // 保存数据
    try {

      long time = System.currentTimeMillis();

      Entity entity = new Entity(Instructor.SQL_TABLE_NAME);
      entity.put(Instructor.SQL_NAME, name);
      entity.put(Instructor.SQL_AVATAR, avatar);
      entity.put(Instructor.SQL_PROFILE, profile);
      entity.put(Instructor.SQL_CONTACTS, contacts);
      entity.put(Instructor.SQL_CREATE_TIME, time);
      entity.put(Instructor.SQL_STATUS, 1);
      entity = entityDao.saveAndReturn(entity);

      return ResponseBuilder.ok(entity.getId());

    } catch (Throwable t) {

      return ResponseBuilder.error(50000, "讲师注册失败，请稍后再试。");
    }
  }

  private Map<String, Object> checkParameters(
          String name,
          String Avatar,
          String contacts,
          String profile) {

    if (StringUtils.isEmpty(name)) {
      return ResponseBuilder.error(13100, "请输入你的名字。");
    }

    if (StringUtils.isEmpty(contacts)) {
      return ResponseBuilder.error(13120, "请输入你的联系方式");
    }

    if (StringUtils.isEmpty(profile)) {
      return ResponseBuilder.error(13130, "请介绍一下你自己");
    }

    return ResponseBuilder.OK;
  }
}
