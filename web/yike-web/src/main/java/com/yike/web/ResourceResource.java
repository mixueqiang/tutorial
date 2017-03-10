package com.yike.web;

import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.view.Viewable;
import com.yike.dao.mapper.ResourceRowMapper;
import com.yike.dao.mapper.SkillRowMapper;
import com.yike.model.Entity;
import com.yike.model.Resource;
import com.yike.model.Skill;
import com.yike.model.User;
import com.yike.util.ResponseBuilder;

/**
 * @author mixueqiang
 * @since Feb 28, 2017
 *
 */
@Path("/resource")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ResourceResource extends BaseResource {

  @GET
  @Path("{id}")
  @Produces(MediaType.TEXT_HTML)
  public Response get(@PathParam("id") long resourcId) {
    Resource resource = entityDao.get("resource", resourcId, ResourceRowMapper.getInstance());
    if (resource == null || resource.isDisabled()) {
      request.setAttribute("_blank", true);
      request.setAttribute("_error", "资源未找到。");
      return Response.ok(new Viewable("resource")).build();
    }

    request.setAttribute("resource", resource);

    Skill skill = entityDao.get("skill", resource.getSkillId(), SkillRowMapper.getInstance());
    request.setAttribute("skill", skill);

    return Response.ok(new Viewable("resource")).build();
  }

  @POST
  @Produces(APPLICATION_JSON)
  public Map<String, Object> publish(@FormParam("skillId") long skillId, @FormParam("title") String title, @FormParam("content") String content, @FormParam("contact") String contact) {
    if (skillId <= 0) {
      return ResponseBuilder.error(50000, "技能不存在。");
    }
    if (StringUtils.isEmpty(title)) {
      return ResponseBuilder.error(50000, "请选择内容简介。");
    }
    if (StringUtils.isEmpty(content)) {
      return ResponseBuilder.error(50000, "请选择百度网盘或URL地址。");
    }

    long time = System.currentTimeMillis();
    User user = getSessionUser();

    Entity entity = new Entity("resource");
    if (user != null) {
      entity.set("userId", user.getId());
    }
    entity.set("skillId", skillId);
    entity.set("title", title).set("content", content).set("contact", contact);
    entity.set("status", 1).set("createTime", time);
    entityDao.save(entity);

    if (user != null) {
      entity = new Entity("skill_user");
      entity.set("userId", user.getId());
      entity.set("skillId", skillId).set("contact", contact);
      entity.set("status", 1).set("createTime", time);
      entityDao.save(entity);
    }

    return ResponseBuilder.OK;
  }

}
