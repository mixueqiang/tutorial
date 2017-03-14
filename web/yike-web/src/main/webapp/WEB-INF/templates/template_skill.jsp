<!DOCTYPE HTML><%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/commons/head.jsp"%>
<d:head />
</head>
<body>
  <header class="navbar" id="header" role="banner">
    <div class="navbar-header">
      <div class="container">
        <a class="navbar-brand xxl" href="/skill">技能</a>
        <ul class="nav pull-right">
          <c:choose>
            <c:when test="${not empty _user}">
              <li class="pull-left"><a href="/settings/profile">${_user.username}</a></li>
              <li class="pull-left col-space-2"><a href="/signout">退出</a></li>
            </c:when>
            <c:otherwise>
              <li class="pull-left col-space-2"><a href="/signin?to=/skill">登录</a></li>
            </c:otherwise>
          </c:choose>
        </ul>
      </div>
    </div>
  </header>
  <%@ include file="/WEB-INF/commons/message.jsp"%>
  <c:if test="${not _blank}">
    <d:body />
  </c:if>
</body>
</html>