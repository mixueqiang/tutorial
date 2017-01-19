package com.yike.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.view.Viewable;
import com.yike.dao.BaseDao;
import com.yike.model.Entity;
import com.yike.util.Pair;

/**
 * @author mixueqiang
 * @since Jan 19, 2017
 *
 */
@Path("/fu")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class FuResource extends BaseResource {
  private static final Map<String, Pair<String, String>> FU_COLLECTION = new HashMap<String, Pair<String, String>>();
  static {
    FU_COLLECTION.put("fu1", new Pair<String, String>("爱国福", "fu1.png"));
    FU_COLLECTION.put("fu2", new Pair<String, String>("富强福", "fu2.png"));
    FU_COLLECTION.put("fu3", new Pair<String, String>("和谐福", "fu3.png"));
    FU_COLLECTION.put("fu4", new Pair<String, String>("友善福", "fu4.png"));
    FU_COLLECTION.put("fu5", new Pair<String, String>("敬业福", "fu5.png"));
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response index() {
    return Response.ok(new Viewable("index")).build();
  }

  @GET
  @Path("exchange")
  @Produces(MediaType.TEXT_HTML)
  public Response exchange(@QueryParam("fu") String name) {
    Pair<String, String> fu = FU_COLLECTION.get(name);
    if (fu == null) {
      request.setAttribute("_error", "点错了吧？福倒了？");
      request.setAttribute("_blank", true);
      return Response.ok(new Viewable("exchange")).build();
    }
    request.setAttribute("fu", fu);

    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put("status", 1);
    Map<Pair<String, String>, Object> offsets = new HashMap<Pair<String, String>, Object>();
    offsets.put(new Pair<String, String>(name, BaseDao.ORDER_OPTION_ASC), 0L);
    List<Entity> fus = entityDao.findByOffset("user_fu", condition, offsets, 20);
    request.setAttribute("fus", fus);

    return Response.ok(new Viewable("exchange")).build();
  }

}
