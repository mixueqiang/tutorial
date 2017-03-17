package com.yike.web;

import java.util.HashMap;
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
  public Map<String, Object> save(@FormParam("skillId") long skillId, @FormParam("title") String title, @FormParam("content") String content, @FormParam("url") String url,
      @FormParam("password") String password) {
    if (skillId <= 0) {
      return ResponseBuilder.error(50000, "技能不存在。");
    }
    if (StringUtils.isEmpty(title)) {
      return ResponseBuilder.error(50000, "请输入资料名称。");
    }
    if (StringUtils.length(content) > 200) {
      return ResponseBuilder.error(50000, "资源介绍不能超过200个字。");
    }
    if (StringUtils.isEmpty(url)) {
      return ResponseBuilder.error(50000, "请选择网盘链接或网址。");
    }

    long time = System.currentTimeMillis();
    User user = getSessionUser();

    Entity entity = new Entity("resource");
    if (user != null) {
      entity.set("userId", user.getId());
    }
    entity.set("skillId", skillId);
    entity.set("title", title).set("content", content).set("url", url).set("password", password);
    entity.set("status", 0).set("createTime", time);
    entityDao.save(entity);

    if (user != null) {
      entity = new Entity("skill_user");
      entity.set("userId", user.getId()).set("skillId", skillId);
      entity.set("status", 1).set("createTime", time);
      entityDao.save(entity);
    }

    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put("skillId", skillId);
    int resourceCount = entityDao.count("resource", condition);
    entityDao.update("skill", "id", skillId, "resourceCount", resourceCount);

    return ResponseBuilder.OK;
  }

}
