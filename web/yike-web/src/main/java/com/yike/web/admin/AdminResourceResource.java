package com.yike.web.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.view.Viewable;
import com.yike.dao.mapper.ResourceRowMapper;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.Resource;
import com.yike.model.User;
import com.yike.util.PageNumberUtils;
import com.yike.util.Pair;
import com.yike.web.BaseResource;

/**
 * @author mixueqiang
 * @since Mar 15, 2017
 *
 */
@Path("/admin/resource")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AdminResourceResource extends BaseResource {

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index(@QueryParam("skillId") long skillId, @QueryParam("page") int page) {
    page = page > 0 ? page : 1;

    Map<String, Object> condition = new HashMap<String, Object>();
    if (skillId > 0) {
      condition.put("skillId", skillId);
    }
    Pair<Integer, List<Resource>> result = entityDao.findAndCount("resource", condition, page, 20, ResourceRowMapper.getInstance());

    List<Resource> resources = result.right;
    for (Resource resource : resources) {
      User user = entityDao.get("user", resource.getUserId(), UserRowMapper.getInstance());
      if (user != null) {
        resource.getProperties().put("user", user);
      }
    }
    request.setAttribute("resources", resources);

    // pagination.
    String uri = request.getRequestURI() + "?";
    String queryString = request.getQueryString();
    if (StringUtils.isNotEmpty(queryString)) {
      uri += queryString + "&page=";
    } else {
      uri += "page=";
    }

    uri = StringUtils.replace(uri, "page=" + page + "&", "");
    request.setAttribute("uriPrefix", uri);

    Pair<List<Integer>, Integer> pages = PageNumberUtils.generate(page, result.left);
    request.setAttribute("currentPage", page);
    request.setAttribute("pages", pages.left);
    request.setAttribute("lastPage", pages.right);

    return Response.ok(new Viewable("index")).build();
  }

}
