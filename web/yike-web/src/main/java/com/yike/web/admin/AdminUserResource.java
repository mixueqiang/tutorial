package com.yike.web.admin;

import com.sun.jersey.api.view.Viewable;
import com.yike.dao.BaseDao;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.User;
import com.yike.util.PageNumberUtils;
import com.yike.util.Pair;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author mixueqiang
 * @since May 24, 2014
 */
@Path("/admin/user")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AdminUserResource extends BaseResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response index(@QueryParam("p") int page, @DefaultValue("20") @QueryParam("s") int size,
                          @QueryParam("o") String orderBy) {
        page = page > 0 ? page : 1;
        orderBy = StringUtils.isEmpty(orderBy) ? BaseDao.ORDER_BY_ID : orderBy;

        int count = entityDao.count("user");
        List<User> users = entityDao.get("user", page, size, UserRowMapper.getInstance(), orderBy,
                BaseDao.ORDER_OPTION_DESC);
        request.setAttribute("users", users);

        // pagination.
        String uri = request.getRequestURI() + "?";
        String queryString = request.getQueryString();
        if (StringUtils.isNotEmpty(queryString)) {
            uri += queryString + "&p=";
        } else {
            uri += "p=";
        }

        uri = StringUtils.replace(uri, "p=" + page + "&", "");
        request.setAttribute("uriPrefix", uri);

        Pair<List<Integer>, Integer> pages = PageNumberUtils.generate(page, count);
        request.setAttribute("currentPage", page);
        request.setAttribute("pages", pages.left);
        request.setAttribute("lastPage", pages.right);

        return Response.ok(new Viewable("index")).build();
    }

}
