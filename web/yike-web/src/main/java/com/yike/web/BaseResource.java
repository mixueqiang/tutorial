package com.yike.web;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.yike.dao.BidDao;
import com.yike.dao.ProjectDao;
import com.yike.dao.UserDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.WebUtils;

import com.yike.dao.EntityDao;
import com.yike.dao.mapper.CategoryRowMapper;
import com.yike.model.Category;
import com.yike.model.User;
import com.yike.service.SessionService;

/**
 * @author mixueqiang
 * @since Mar 7, 2014
 */
public abstract class BaseResource {
  private static final Log LOG = LogFactory.getLog(BaseResource.class);
  public final static String APPLICATION_JSON = "application/json; charset=utf-8";

  protected static Map<Long, Category> CATEGORIES = new TreeMap<Long, Category>(new Comparator<Long>() {
    @Override
    public int compare(Long o1, Long o2) {
      return o1.compareTo(o2);
    }
  });

  @Resource
  protected EntityDao entityDao;
  @Resource
  protected BidDao bidDao;
  @Resource
  protected ProjectDao projectDao;
  @Resource
  protected UserDao userDao;
  @Resource
  protected SessionService sessionService;

  @Context
  protected HttpServletRequest request;

  public Map<Long, Category> getCategories() {
    initializeCategories();

    return CATEGORIES;
  }

  public Category getCategory(long categoryId) {
    initializeCategories();

    Category category = CATEGORIES.get(categoryId);
    if (category == null) {
      return CATEGORIES.get(100L);
    }

    return category;
  }

  public Object getSessionAttribute(String name) {
    return WebUtils.getSessionAttribute(request, name);
  }

  public User getSessionUser() {
    return (User) getSessionAttribute("_user");
  }

  public long getSessionUserId() {
    User user = getSessionUser();
    if (user != null) {
      return user.getId();
    }

    return 0L;
  }

  public Response redirect(String to) {
    try {
      return Response.seeOther(new URI(to)).build();
    } catch (Throwable t) {
      return null;
    }
  }

  public void setSessionAttribute(String name, Object value) {
    WebUtils.setSessionAttribute(request, name, value);
  }

  public Response signinAndGoback() {
    return signinAndGoto(request.getRequestURI(), request.getQueryString());
  }

  public Response signinAndGoto(String to) {
    try {
      return Response.seeOther(new URI("/signin?to=" + URLEncoder.encode(to, "utf-8"))).build();
    } catch (Throwable t) {
      return null;
    }
  }

  public Response signinAndGoto(String requestUri, String queryString) {
    String to = requestUri;
    if (StringUtils.isNotEmpty(queryString)) {
      to += "?" + queryString;
    }

    return signinAndGoto(to);
  }

  private void initializeCategories() {
    try {
      if (CATEGORIES.isEmpty()) {
        synchronized (CATEGORIES) {
          List<Category> list = entityDao.find("category", "status", 1, CategoryRowMapper.getInstance(), 1, 100);
          for (Category category : list) {
            CATEGORIES.put(category.getId(), category);
          }
        }
      }
    } catch (Throwable t) {
      LOG.error("Failed to load category data.", t);
    }
  }

}
