package com.yike.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.view.Viewable;
import com.yike.dao.BaseDao;
import com.yike.dao.mapper.EntityRowMapper;
import com.yike.dao.mapper.SkillRowMapper;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.Entity;
import com.yike.model.Skill;
import com.yike.model.User;

/**
 * 
 * @author mixueqiang
 * @since Jan 17, 2017
 *
 */
@Path("/skill")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SkillResource extends BaseResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put("categoryId", 1);
    List<Skill> skills = entityDao.find("skill", condition, 1, SkillRowMapper.getInstance());
    request.setAttribute("skills", skills);

    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("{name}")
  @Produces(MediaType.TEXT_HTML)
  public Response get(@PathParam("name") String name) {
    Skill skill = entityDao.findOne("skill", "slug", name, SkillRowMapper.getInstance());
    if (skill == null || skill.isDisabled()) {
      // TODO:
    }
    request.setAttribute("skill", skill);

    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put("skillId", skill.getId());
    condition.put("status", 1);
    List<Entity> entities = entityDao.find("skill_user", condition, 1, 11, EntityRowMapper.getInstance(), "rank", BaseDao.ORDER_OPTION_DESC);
    List<User> users = new ArrayList<User>();
    for (Entity entity : entities) {
      User user = entityDao.get("user", entity.getLong("userId"), UserRowMapper.getInstance());
      if (user != null) {
        user.getProperties().put("count", entity.getInt("rank"));
        users.add(user);
      }
    }
    request.setAttribute("users", users);

    return Response.ok(new Viewable("skill")).build();
  }

}
