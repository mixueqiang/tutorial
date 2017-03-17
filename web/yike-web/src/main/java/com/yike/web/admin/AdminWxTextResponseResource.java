package com.yike.web.admin;

import com.sun.jersey.api.view.Viewable;
import com.yike.Constants;
import com.yike.dao.BaseDao;
import com.yike.dao.mapper.WxTextResponseRowMapper;
import com.yike.model.Entity;
import com.yike.model.User;
import com.yike.model.WxTextResponse;
import com.yike.task.WxServiceScheduler;
import com.yike.util.PageNumberUtils;
import com.yike.util.Pair;
import com.yike.util.ResponseBuilder;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/3/17
 */

@Path("/admin/wxtextresponse")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AdminWxTextResponseResource extends BaseResource {

    @Resource
    protected WxServiceScheduler wxServiceScheduler;

    @GET
    @Path("it")
    @Produces(MediaType.TEXT_HTML)
    public Response it(@QueryParam("p") int page, @DefaultValue("20") @QueryParam("s") int size,
                       @QueryParam("o") String orderBy) {

        page = page > 0 ? page : 1;
        orderBy = StringUtils.isEmpty(orderBy) ? BaseDao.ORDER_BY_ID : orderBy;
        int count = entityDao.count("wx_text_response");
        List<WxTextResponse> responses = null;

        responses = entityDao.find("wx_text_response", "status", 1, WxTextResponseRowMapper.getInstance(), page, size, orderBy, BaseDao.ORDER_OPTION_DESC);

        request.setAttribute("responses", responses);

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

        Pair<List<Integer>, Integer> pages;
        pages = PageNumberUtils.generate(page, count);
        request.setAttribute("count", count);
        request.setAttribute("currentPage", page);
        request.setAttribute("pages", pages.left);
        request.setAttribute("lastPage", pages.right);

        return Response.ok(new Viewable("it")).build();

    }

    @GET
    @Path("yike")
    @Produces(MediaType.TEXT_HTML)
    public Response yike(@QueryParam("p") int page, @DefaultValue("20") @QueryParam("s") int size,
                         @QueryParam("o") String orderBy) {

        page = page > 0 ? page : 1;
        orderBy = StringUtils.isEmpty(orderBy) ? BaseDao.ORDER_BY_ID : orderBy;
        int count = entityDao.count("wx_yike_text_response");
        List<WxTextResponse> responses = null;

        responses = entityDao.find("wx_yike_text_response", "status", 1, WxTextResponseRowMapper.getInstance(), page, size, orderBy, BaseDao.ORDER_OPTION_DESC);

        request.setAttribute("responses", responses);

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

        Pair<List<Integer>, Integer> pages;
        pages = PageNumberUtils.generate(page, count);
        request.setAttribute("count", count);
        request.setAttribute("currentPage", page);
        request.setAttribute("pages", pages.left);
        request.setAttribute("lastPage", pages.right);

        return Response.ok(new Viewable("yike")).build();
    }

    @POST
    @Path("it/edit")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> itEdit(@FormParam("id") long id,
                                      @FormParam("target") String target,
                                      @FormParam("result") String result) {

        User user = getSessionUser();
        if (!user.isAdmin()) {
            return ResponseBuilder.error(50000, "Permission denied");
        }

        if (id == 0 || StringUtils.isEmpty(target) || StringUtils.isEmpty(result)) {
            return ResponseBuilder.error(50000, "数据不合法。");
        }

        Map<String, Object> updateValues = new HashMap<String, Object>();

        updateValues.put("result", result);
        updateValues.put("target", target);

        try {
            entityDao.update("wx_text_response", "id", id, updateValues);
            wxServiceScheduler.setupWxResponses();
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            return ResponseBuilder.error(t);
        }
    }

    @POST
    @Path("yike/edit")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> yikeEdit(@FormParam("id") long id,
                                        @FormParam("target") String target,
                                        @FormParam("result") String result) {

        User user = getSessionUser();
        if (!user.isAdmin()) {
            return ResponseBuilder.error(50000, "Permission denied");
        }

        if (id == 0 || StringUtils.isEmpty(target) || StringUtils.isEmpty(result)) {
            return ResponseBuilder.error(50000, "数据不合法。");
        }

        Map<String, Object> updateValues = new HashMap<String, Object>();

        updateValues.put("result", result);
        updateValues.put("target", target);

        try {
            entityDao.update("wx_yike_text_response", "id", id, updateValues);
            wxServiceScheduler.setupWxResponses();
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            return ResponseBuilder.error(t);
        }
    }

    @POST
    @Path("it")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> itAdd(@FormParam("target") String target,
                                     @FormParam("result") String result) {

        User user = getSessionUser();
        if (!user.isAdmin()) {
            return ResponseBuilder.error(50000, "Permission denied");
        }

        if (StringUtils.isEmpty(target) || StringUtils.isEmpty(result)) {
            return ResponseBuilder.error(50000, "数据不合法");
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("status", Constants.STATUS_OK);
        condition.put("target", target);

        try {
            if (entityDao.exists("wx_text_response", condition)) {
                return ResponseBuilder.error(50000, "已存在关于" + target + "的回复");
            }

            Entity entity = new Entity("wx_text_response");
            entity
                    .set("status", Constants.STATUS_OK)
                    .set("result", result)
                    .set("target", target)
                    .set("createTime", System.currentTimeMillis());

            entityDao.save(entity);
            wxServiceScheduler.setupWxResponses();
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            return ResponseBuilder.error(t);
        }
    }

    @POST
    @Path("yike")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> yikeAdd(@FormParam("target") String target,
                                       @FormParam("result") String result) {

        User user = getSessionUser();
        if (!user.isAdmin()) {
            return ResponseBuilder.error(50000, "Permission denied");
        }

        if (StringUtils.isEmpty(target) || StringUtils.isEmpty(result)) {
            return ResponseBuilder.error(50000, "数据不合法");
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("status", Constants.STATUS_OK);
        condition.put("target", target);

        try {
            if (entityDao.exists("wx_yike_text_response", condition)) {
                return ResponseBuilder.error(50000, "已存在关于" + target + "的回复");
            }

            Entity entity = new Entity("wx_yike_text_response");
            entity
                    .set("status", Constants.STATUS_OK)
                    .set("result", result)
                    .set("target", target)
                    .set("createTime", System.currentTimeMillis());

            entityDao.save(entity);
            wxServiceScheduler.setupWxResponses();
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            return ResponseBuilder.error(t);
        }
    }

    @POST
    @Path("it/delete")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> itDelete(@FormParam("id") long id) {

        User user = getSessionUser();
        if (!user.isAdmin()) {
            return ResponseBuilder.error(50000, "Permission denied");
        }

        if (id == 0) {
            return ResponseBuilder.error(50000, "数据不合法");
        }

        try {
            entityDao.update("wx_text_response", "id", id, "status", Constants.STATUS_DELETED_BY_ADMIN);
            wxServiceScheduler.setupWxResponses();
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            return ResponseBuilder.error(t);
        }
    }

    @POST
    @Path("yike/delete")
    @Produces(APPLICATION_JSON)
    public Map<String, Object> yikeDelete(@FormParam("id") long id) {

        User user = getSessionUser();
        if (!user.isAdmin()) {
            return ResponseBuilder.error(50000, "Permission denied");
        }

        if (id == 0) {
            return ResponseBuilder.error(50000, "数据不合法");
        }

        try {
            entityDao.update("wx_yike_text_response", "id", id, "status", Constants.STATUS_DELETED_BY_ADMIN);
            wxServiceScheduler.setupWxResponses();
            return ResponseBuilder.OK;
        } catch (Throwable t) {
            return ResponseBuilder.error(t);
        }
    }
}
