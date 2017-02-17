<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<header class="navbar" id="header" role="banner">
  <div class="navbar-header">
    <div class="container">
      <a class="navbar-brand xl" href="/admin">一课上手</a>
      <ul class="nav pull-right">
        <li class="pull-left col-space-2"><a href="/settings/profile">${_user.username}</a></li>
        <li class="pull-left"><a href="/signout">退出登录</a></li>
      </ul>
    </div>
  </div>
</header>