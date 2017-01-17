package com.yike.web.api.v1;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yike.dao.mapper.SkillRowMapper;
import com.yike.model.Entity;
import com.yike.model.Skill;
import com.yike.model.User;
import com.yike.util.ResponseBuilder;
import com.yike.web.BaseResource;

/**
 * 
 * @author mixueqiang
 * @since Jan 18, 2017
 *
 */
@Path("/api/v1/skill")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiSkillResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiSkillResource.class);

  @GET
  @Path("{id}")
  @Produces(APPLICATION_JSON)
  public Map<String, Object> get(@PathParam("id") long skillId) {
    Skill skill = entityDao.get("skill", skillId, SkillRowMapper.getInstance());
    if (skill == null || skill.isDisabled()) {
      return ResponseBuilder.error(60404, "未找到技能。");
    }

    return ResponseBuilder.ok(skill);
  }

  @POST
  @Path("tag")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> tag(@FormParam("id") long id) {
    User user = getSessionUser();
    if (null == user) {
      return ResponseBuilder.ERR_NEED_LOGIN;
    }

    long time = System.currentTimeMillis();
    try {
      Map<String, Object> condition = new HashMap<String, Object>();
      condition.put("skillId", id);
      condition.put("userId", user.getId());
      if (!entityDao.exists("skill_user", condition)) {
        Entity entity = new Entity("skill_user");
        entity.set("skillId", id).set("userId", user.getId());
        entity.set("status", 1).set("createTime", time);

        entityDao.save(entity);
      }

      return ResponseBuilder.OK;

    } catch (Throwable t) {
      LOG.error("提交失败！", t);
      return ResponseBuilder.error(50000, "提交失败，请稍后再试。");
    }
  }

}
