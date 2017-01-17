package com.yike.web;

import java.util.List;

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
import com.yike.dao.mapper.SkillRowMapper;
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
    List<Skill> skills = entityDao.get("skill", 1, SkillRowMapper.getInstance());
    request.setAttribute("skills", skills);

    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("{name}")
  @Produces(MediaType.TEXT_HTML)
  public Response get(@PathParam("name") String name) {
    Skill skill = entityDao.findOne("skill", "slug", name, SkillRowMapper.getInstance());
    request.setAttribute("skill", skill);

    return Response.ok(new Viewable("skill")).build();
  }

}
