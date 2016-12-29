package com.yike.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.yike.model.User;
import com.yike.util.RandomUtil;
import com.sun.jersey.api.view.Viewable;
import com.yike.dao.mapper.CourseApplicationRowMapper;
import com.yike.dao.mapper.CourseRowMapper;
import com.yike.model.Course;
import com.yike.model.CourseApplication;

/**
 * @author mixueqiang
 * @since Dec 19, 2016
 *
 */
@Path("/order")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class OrderResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(OrderResource.class);

  @GET
  @Path("pay")
  @Produces(MediaType.TEXT_HTML)
  public Response pay(@QueryParam("type") int type, @QueryParam("dataId") long dataId) {
    User user = getSessionUser();
    if (user == null) {
      return signinAndGoback();
    }

    Course course = entityDao.get(Course.SQL_TABLE_NAME, dataId, CourseRowMapper.getInstance());
    if (course == null || course.isDisabled()) {
      request.setAttribute("_error", "未找到课程，或课程已经结束招生。你可以尝试重新报名，如果仍然遇到问题，请联系客服进行解决。");
      return Response.ok(new Viewable("alipay_submit")).build();
    }

    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put(CourseApplication.SQL_USER_ID, user.getId());
    condition.put(CourseApplication.SQL_COURSE_ID, dataId);
    CourseApplication courseApplication = entityDao.findOne(CourseApplication.SQL_TABLE_NAME, condition, CourseApplicationRowMapper.getInstance());
    if (courseApplication == null || courseApplication.isDisabled()) {
      request.setAttribute("_error", "无效的报名信息。你可以尝试重新报名，如果仍然遇到问题，请联系客服进行解决。");
      return Response.ok(new Viewable("alipay_submit")).build();
    }

    long time = System.currentTimeMillis();
    String orderId = courseApplication.getOrderId();
    if (StringUtils.isEmpty(orderId)) {
      orderId = "C" + course.getId() + "_" + time + "_" + RandomUtil.randomString(6);
      Map<String, Object> updateValues = new HashMap<String, Object>();
      updateValues.put(CourseApplication.SQL_ORDER_ID, orderId);
      updateValues.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_NON_PAYMENT);
      updateValues.put(CourseApplication.SQL_UPDATE_TIME, time);
      entityDao.update(CourseApplication.SQL_TABLE_NAME, CourseApplication.SQL_ID, courseApplication.getId(), updateValues);
    }

    String outTradeNo = orderId;
    String subject = course.getName();
    String totalFee = courseApplication.getPrice() + "";
    String body = "购买课程：" + course.getName();

    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("service", AlipayConfig.service);
    parameters.put("partner", AlipayConfig.partner);
    parameters.put("seller_id", AlipayConfig.seller_id);
    parameters.put("_input_charset", AlipayConfig.input_charset);
    parameters.put("payment_type", AlipayConfig.payment_type);
    parameters.put("notify_url", AlipayConfig.notify_url);
    parameters.put("return_url", AlipayConfig.return_url);
    parameters.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
    parameters.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
    parameters.put("out_trade_no", outTradeNo);
    parameters.put("subject", subject);
    parameters.put("total_fee", totalFee);
    parameters.put("body", body);

    try {
      String html = AlipaySubmit.buildRequest(parameters, "get", "确认");
      request.setAttribute("html", html);
      request.setAttribute("_msg", "正在跳转到支付页面...");
      return Response.ok(new Viewable("alipay_submit")).build();

    } catch (Throwable t) {
      LOG.error("Failed to submit alipay request.", t);
      request.setAttribute("_error", "提交支付请求失败。您可以尝试重新报名，如果仍然遇到问题，请联系客服进行解决。");
      return Response.ok(new Viewable("alipay_submit")).build();
    }
  }

  @GET
  @Path("pay/problems")
  @Produces(MediaType.TEXT_HTML)
  public Response problems() {
    return Response.ok(new Viewable("pay_problems")).build();
  }

  @GET
  @Path("pay/result")
  @Produces(MediaType.TEXT_HTML)
  public Response result(@QueryParam("type") int type, @QueryParam("dataId") long dataId) {
    User user = getSessionUser();
    if (user == null) {
      return signinAndGoback();
    }

    Map<String, Object> condition = new HashMap<String, Object>();
    condition.put(CourseApplication.SQL_USER_ID, user.getId());
    condition.put(CourseApplication.SQL_COURSE_ID, dataId);
    condition.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);
    CourseApplication courseApplication = entityDao.findOne(CourseApplication.SQL_TABLE_NAME, condition, CourseApplicationRowMapper.getInstance());

    if (courseApplication == null) {
      Course course = entityDao.get(Course.SQL_TABLE_NAME, dataId, CourseRowMapper.getInstance());
      request.setAttribute("course", course);
      return Response.ok(new Viewable("pay_result_error")).build();
    }

    return redirect("/courses/as_a_student");
  }

  @GET
  @Path("pay/callback")
  @Produces(MediaType.TEXT_HTML)
  public Response callback(@QueryParam("out_trade_no") String outTradeNo, @QueryParam("trade_no") String tradeNo, @QueryParam("trade_status") String tradeStatus) {
    try {
      Map<String, String> parameters = new HashMap<String, String>();
      Map<?, ?> requestParams = request.getParameterMap();
      for (Iterator<?> iterator = requestParams.keySet().iterator(); iterator.hasNext();) {
        String name = (String) iterator.next();
        String[] values = (String[]) requestParams.get(name);
        String valueString = "";
        for (int i = 0; i < values.length; i++) {
          String value = values[i];
          valueString += value;
          if (i < values.length - 1) {
            valueString += ",";
          }
        }

        valueString = new String(valueString.getBytes("ISO-8859-1"), "utf-8");
        parameters.put(name, valueString);
      }

      if (AlipayNotify.verify(parameters)) {
        if (StringUtils.equals(tradeStatus, "TRADE_FINISHED") || StringUtils.equals(tradeStatus, "TRADE_SUCCESS")) {
          CourseApplication courseApplication = entityDao.findOne(CourseApplication.SQL_TABLE_NAME, CourseApplication.SQL_ORDER_ID, outTradeNo, CourseApplicationRowMapper.getInstance());
          if (courseApplication == null) {
            request.setAttribute("errorCode", 50404);
            LOG.error("Can not find alipay trade no: " + outTradeNo);
          }

          long time = System.currentTimeMillis();
          Map<String, Object> updateValues = new HashMap<String, Object>();
          updateValues.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);
          updateValues.put(CourseApplication.SQL_UPDATE_TIME, time);
          entityDao.update(CourseApplication.SQL_TABLE_NAME, CourseApplication.SQL_ID, courseApplication.getId(), updateValues);

          request.setAttribute("errorCode", 0);
        }
      }

    } catch (Throwable t) {
      request.setAttribute("errorCode", 50000);
      LOG.error("Failed to process alipay callback request.", t);
    }

    return Response.ok(new Viewable("alipay_callback")).build();
  }

  @POST
  @Path("pay/notify")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  public String notify(@FormParam("out_trade_no") String outTradeNo, @FormParam("trade_no") String tradeNo, @FormParam("trade_status") String tradeStatus,
      MultivaluedMap<String, String> parameterMap) {
    try {
      Map<String, String> parameters = new HashMap<String, String>();
      for (String name : parameterMap.keySet()) {
        List<String> values = parameterMap.get(name);
        String valueString = "";
        for (int i = 0; i < values.size(); i++) {
          String value = values.get(i);
          valueString += value;
          if (i < values.size() - 1) {
            valueString += ",";
          }
        }

        parameters.put(name, valueString);
      }

      if (AlipayNotify.verify(parameters)) {
        if (StringUtils.equals(tradeStatus, "TRADE_FINISHED") || StringUtils.equals(tradeStatus, "TRADE_SUCCESS")) {
          CourseApplication courseApplication = entityDao.findOne(CourseApplication.SQL_TABLE_NAME, CourseApplication.SQL_ORDER_ID, outTradeNo, CourseApplicationRowMapper.getInstance());
          if (courseApplication == null) {
            LOG.error("Can not find alipay trade no: " + outTradeNo);
          }

          long time = System.currentTimeMillis();
          Map<String, Object> updateValues = new HashMap<String, Object>();
          updateValues.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);
          updateValues.put(CourseApplication.SQL_UPDATE_TIME, time);
          entityDao.update(CourseApplication.SQL_TABLE_NAME, CourseApplication.SQL_ID, courseApplication.getId(), updateValues);

          // 更新申请人数
          Map<String, Object> courseCountCondition = new HashMap<String, Object>();
          courseCountCondition.put(CourseApplication.SQL_COURSE_ID, courseApplication.getCourseId());
          courseCountCondition.put(CourseApplication.SQL_PROGRESS, CourseApplication.PROGRESS_PAID);
          int count = entityDao.count(CourseApplication.SQL_TABLE_NAME, courseCountCondition);
          Map<String, Object> courseUpdateCondition = new HashMap<String, Object>();
          courseUpdateCondition.put(Course.SQL_ID, courseApplication.getCourseId());
          entityDao.update(Course.SQL_TABLE_NAME, courseUpdateCondition, Course.SQL_COUNT_THIS, count);
        }

        return "success";
      }

    } catch (Throwable t) {
      LOG.error("Failed to process alipay notification request.", t);
    }

    return "fail";
  }

}
