package com.yike.web;

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
import com.yike.model.Entity;
import com.yike.model.Skill;

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
    condition.put("categoryId", 102);
    condition.put("status", 1);
    List<Skill> skills = entityDao.find("skill", condition, 1, 100, SkillRowMapper.getInstance());
    request.setAttribute("skills", skills);

    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("{name}")
  @Produces(MediaType.TEXT_HTML)
  public Response get(@PathParam("name") String name) {
    Skill skill = entityDao.findOne("skill", "slug", name, SkillRowMapper.getInstance());
    if (skill == null || skill.isDisabled()) {
      request.setAttribute("_blank", true);
      request.setAttribute("_error", "技能未找到。");
      return Response.ok(new Viewable("skill")).build();
    }

    request.setAttribute("skill", skill);

    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put("skillId", skill.getId());
    condition.put("status", 1);
    List<Entity> resources = entityDao.find("resource", condition, 1, 20, EntityRowMapper.getInstance(), BaseDao.ORDER_OPTION_DESC);
    request.setAttribute("resources", resources);

    return Response.ok(new Viewable("skill")).build();
  }

}
