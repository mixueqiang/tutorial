<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>支付结果 | 一课上手</title>

<div class="container">
  <div class="row row-space-top-4">
    <div class="col-md-8 col-md-offset-2 col-sm-8 col-sm-offset-2 col-xs-12">
      <div class="row">
        <div class="col-md-2 col-sm-3">
          <c:choose>
            <c:when test="${errorCode eq 0}">
              <div class="icon-lg icon-center">
                <span class="glyphicon glyphicon-ok-circle" aria-hidden="true"></span>
              </div>
            </c:when>
            <c:otherwise>
              <div class="icon-lg icon-center">
                <span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
        <div class="col-md-10 col-sm-9">
          <c:choose>
            <c:when test="${errorCode eq 0}">
              <div class="section">
                <div class="section-title t-content-medium">支付成功。</div>
                <c:if test="${not empty course}">
                  <div class="row-space-top-1">
                    课程：<a href="/course/${course.id}" target="_blank">${course.name}</a>
                  </div>
                </c:if>
                <div class="row-space-top-3">
                  <a class="btn btn-success" href="/courses/as_a_student">查看我的课程</a>
                </div>
              </div>
            </c:when>
            <c:otherwise>
              <div class="section">
                <div class="section-title t-content-medium">支付失败：购买无效或支付遇到问题，请联系客服。</div>
                <c:if test="${not empty course}">
                  <div class="row-space-top-1">
                    <a href="/course/${course.id}" target="_blank">课程：${course.name}</a>
                  </div>
                </c:if>
                <div class="row-space-top-3">
                  <a class="btn btn-danger" href="/courses/as_a_student">查看我的课程</a>
                </div>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>
  </div>
</div>