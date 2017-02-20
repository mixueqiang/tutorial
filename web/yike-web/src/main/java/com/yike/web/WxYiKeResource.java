package com.yike.web;

import com.sun.jersey.api.view.Viewable;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.User;
import com.yike.model.WxUser;
import com.yike.service.WxYiKeUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author ilakeyc
 * @since 2017/2/20
 */
@Path("/wx/binding")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class WxYiKeResource extends BaseResource {

  @Resource
  protected WxYiKeUserService wxYiKeUserService;

  @GET
  @Path("phone")
  @Produces(MediaType.TEXT_HTML)
  public Response binding(@QueryParam("oid") String oid) {
    if (StringUtils.isEmpty(oid)) {
      return null;
    }
    WxUser wxUser = wxYiKeUserService.findByOpenId(oid);
    if (wxUser == null) {
      wxUser = wxYiKeUserService.sync(oid);
    }
    if (wxUser == null) {
      // 没有对应的user
      return null;
    }
    long userId = wxUser.getUserId();
    User user = entityDao.get("user", userId, UserRowMapper.getInstance());
    if (user != null) {
      if (StringUtils.isNotEmpty(user.getPhone())) {
        // 已经注册过
        return null;
      }
    }
    request.setAttribute("oid", oid);
    return Response.ok(new Viewable("setPhone")).build();
  }

  @GET
  @Path("pwd")
  @Produces(MediaType.TEXT_HTML)
  public Response setPwd(@QueryParam("oid") String oid) {
    if (StringUtils.isEmpty(oid)) {
      return null;
    }
    WxUser wxUser = wxYiKeUserService.findByOpenId(oid);
    if (wxUser == null) {
      wxUser = wxYiKeUserService.sync(oid);
    }
    if (wxUser == null) {
      // 没有对应的user
      return null;
    }
    long userId = wxUser.getUserId();
    User user = entityDao.get("user", userId, UserRowMapper.getInstance());
    if (user != null) {
      if (StringUtils.isNotEmpty(user.getPassword())) {
        // 已经注册过
        return null;
      }
    }
    request.setAttribute("oid", oid);
    return Response.ok(new Viewable("setPassword")).build();
  }
}
