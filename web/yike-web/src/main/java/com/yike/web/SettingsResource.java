package com.yike.web;

import com.sun.jersey.api.view.Viewable;
import com.yike.Constants;
import com.yike.dao.mapper.InstructorRowMapper;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.Entity;
import com.yike.model.Instructor;
import com.yike.model.User;
import com.yike.util.ResponseBuilder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.weaver.patterns.HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author mixueqiang
 * @since Aug 19, 2014
 */
@Path("/settings")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SettingsResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(SettingsResource.class);

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    return profile();
  }


  @GET
  @Path("password")
  @Produces(MediaType.TEXT_HTML)
  public Response password() {
    return Response.ok(new Viewable("password")).build();
  }

  @GET
  @Path("profile")
  @Produces(MediaType.TEXT_HTML)
  public Response profile() {
    User user = getSessionUser();
    if (user == null) {
      return signinAndGoback();
    }

    user = entityDao.get("user", user.getId(), UserRowMapper.getInstance());
    setSessionAttribute("_user", user);

    Map<String, Object> instructorFindCondition = new HashMap<String, Object>();
    instructorFindCondition.put(Instructor.SQL_USER_ID, user.getId());
    instructorFindCondition.put(Instructor.SQL_STATUS, Constants.STATUS_OK);
    Instructor instructor = entityDao.findOne(Instructor.SQL_TABLE_NAME, instructorFindCondition, InstructorRowMapper.getInstance());
    if (null != instructor) {
      request.setAttribute("instructor", instructor);
    }
    request.setAttribute("user", user);
    return Response.ok(new Viewable("profile")).build();
  }


  @POST
  @Path("password")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> savePassword(@FormParam("password") String password, @FormParam("newPassword") String newPassword) {
    User user = getSessionUser();
    if (user == null) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    if (StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword)) {
      return ResponseBuilder.error(10700, "缺少参数。");
    }

    Entity entity = entityDao.findOne("user", "id", user.getId());
    if (entity == null) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    if (StringUtils.equals(password, entity.getString("password"))) {
      entityDao.update("user", "id", user.getId(), "password", newPassword);
      return ResponseBuilder.OK;
    }

    return ResponseBuilder.error(10710, "当前密码输入有误，请重新输入。");
  }

  @POST
  @Path("profile")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> saveProfile(@FormParam("username") String username, @FormParam("gender") String gender, @FormParam("avatar") String avatar, @FormParam("profile") String profile) {
    User user = getSessionUser();
    if (user == null) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    if (StringUtils.isEmpty(username)) {
      return ResponseBuilder.error(10106, "昵称不能少于2个字，支持中文、字母、数字和下划线。");
    }
    String regex = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{2,20}$";
    Pattern pattern = Pattern.compile(regex);
    if (!pattern.matcher(username).matches()) {
      return ResponseBuilder.error(10107, "昵称不能少于2个字，支持中文、字母、数字和下划线。");
    }
    if (StringUtils.isNotEmpty(gender) && StringUtils.length(gender) != 1) {
      return ResponseBuilder.error(10121, "性别信息错误，请重新选择性别。");
    }
    if (StringUtils.isNotEmpty(profile) && StringUtils.length(profile) > 100) {
      return ResponseBuilder.error(10122, "个人简介不能超过100个字。");
    }

    try {
      long time = System.currentTimeMillis();
      Map<String, Object> updateValues = new HashMap<String, Object>();
      if (!StringUtils.equals(user.getUsername(), username)) {
        if (entityDao.exists("user", "username", username)) {
          return ResponseBuilder.error(10113, "昵称已经被使用，请重新选择一个昵称。");
        }

        updateValues.put("username", username);
      }
      if (!StringUtils.equals(user.getGender(), gender)) {
        updateValues.put("gender", gender);
      }
      if (StringUtils.isNotEmpty(avatar) && !StringUtils.equals(user.getAvatar(), avatar)) {
        updateValues.put("avatar", avatar);
      }
      if (!StringUtils.equals(user.getProfile(), profile)) {
        updateValues.put("profile", profile);
      }

      if (MapUtils.isEmpty(updateValues)) {
        return ResponseBuilder.error(10110, "没有提交任何修改。");
      }

      updateValues.put("updateTime", time);
      entityDao.update("user", "id", user.getId(), updateValues);

      // 更新Session
      user = entityDao.get("user", user.getId(), UserRowMapper.getInstance());
      setSessionAttribute("_user", user);
      return ResponseBuilder.OK;

    } catch (Throwable t) {
      LOG.error("Failed to update the user profile: " + user.getId(), t);
      return ResponseBuilder.error(50000, "资料更新失败。");
    }
  }

}
