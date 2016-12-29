<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>支付尚未完成 | 翻译资源网</title>

<div class="container">
  <div class="row row-space-top-4">
    <div class="col-md-8 col-md-offset-2 col-sm-10 col-sm-offset-1 col-xs-12">
      <div class="row">
        <div class="col-md-2 col-sm-3">
          <div class="icon-lg icon-center icon-danger">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
          </div>
        </div>
        <div class="col-md-10 col-sm-9">
          <div class="t-content-medium">支付未完成。</div>
          <c:choose>
            <c:when test="${not empty course}">
              <div class="row-space-top-1">
                课程：<a href="/course/${course.id}" target="_blank">${course.name}</a>
              </div>
              <div class="row-space-top-3">
                <a class="btn btn-success btn-pay" href="/order/pay?type=60000&dataId=${course.id}">继续完成支付</a>
              </div>
            </c:when>
            <c:otherwise>
              <div class="row-space-top-1">未找到课程，订单无效。你可以尝试重新报名，如果仍然遇到问题，请联系客服进行解决。</div>
            </c:otherwise>
          </c:choose>
          <div class="row-space-top-3">
            <a class="btn-link" href="/order/pay/problems" target="_blank">支付遇到问题？</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>