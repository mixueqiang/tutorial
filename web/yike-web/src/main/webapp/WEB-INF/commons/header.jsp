<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<header class="navbar" id="header" role="banner">
  <div class="navbar-header">
    <div class="container">
      <a class="navbar-brand xxl" href="/">一课</a>
      <ul class="nav pull-right">
        <li class="pull-left"><a class="btn btn-success" href="/">发布一门实践课程</a></li>
        <c:choose>
          <c:when test="${not empty sessionScope._user}">
            <li class="pull-left col-space-2"><a href="">${_user.phone}</a></li>
            <li class="pull-left col-space-2"><a href="/signout">退出</a></li>
          </c:when>
          <c:otherwise>
            <li class="pull-left col-space-2"><a href="/signin">登录</a></li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </div>
</header>