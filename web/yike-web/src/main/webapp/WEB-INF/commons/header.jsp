<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<header class="navbar" id="header" role="banner">
  <div class="navbar-header">
    <div class="container">
      <a class="navbar-brand xl" href="/">一课上手</a>
      <ul class="nav pull-right">
        <!--<li class="pull-left"><a class="btn btn-success" href="/course/create">发布一门实践课程</a></li>-->
        <c:choose>
          <c:when test="${not empty _user}">
            <div class="dropdown headerDropdown">
              <button class="btn dropdown-toggle dropdownMenu1" type="button" id="dropdownMenu1" data-toggle="dropdown">
                <a href="/dashboard">${_user.username}</a><span class="caret"></span>
              </button>
              <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                <li role="presentation"><a role="menuitem" tabindex="-1" href="/courses">我的课程</a></li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="/settings/profile">个人信息</a></li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="/signout">退出登录</a></li>
              </ul>
            </div>
          </c:when>
          <c:otherwise>
            <li class="pull-left col-space-2"><c:choose>
                <c:when test="${pageContext.request.requestURI eq '/signup' or pageContext.request.requestURI eq '/password/remind'}">
                  <a href="/signin">登录</a>
                </c:when>
                <c:otherwise>
                  <a href="/signin?to=${pageContext.request.requestURI}">登录</a>
                </c:otherwise>
              </c:choose></li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </div>
</header>