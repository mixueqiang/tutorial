<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<header class="navbar" id="header" role="banner">
  <div class="navbar-header">
    <div class="container">
      <a class="navbar-brand xxl" href="/">一课</a>
      <ul class="nav pull-right">
        <!--<li class="pull-left"><a class="btn btn-success" href="/course/create">发布一门实践课程</a></li>-->
        <c:choose>
          <c:when test="${not empty _user}">
            <div class="dropdown headerDropdown">
              <button class="btn dropdown-toggle dropdownMenu1" type="button" id="dropdownMenu1" data-toggle="dropdown">
                <%-- <img alt="${sessionScope._user.username}" title="${sessionScope._user.username},查看更多个人信息" class="img-responsive "src="http://img3.imgtn.bdimg.com/it/u=3799866287,2225466980&fm=23&gp=0.jpg"> --%>
                <a href="/dashboard">${_user.username}</a>
                <span class="caret"></span>
              </button>
              <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                <li role="presentation"><a role="menuitem" tabindex="-1" href="/courses">我的课程</a></li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="/settings/profile">个人信息</a></li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="/signout">退出</a></li>
              </ul>
            </div>
          </c:when>
          <c:otherwise>
            <li class="pull-left col-space-2"><a href="/signin">登录</a></li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </div>
</header>