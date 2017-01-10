<!DOCTYPE HTML><%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/commons/head.jsp"%>
<d:head />
</head>
<body>
  <a class="sr-only" href="#content">Skip to main content</a>
  <%@ include file="/WEB-INF/commons/header.jsp"%>
  <%@ include file="/WEB-INF/commons/message.jsp"%>
  <div class="container row-space-top-4 row-space-4">
    <div class="row">
      <div class="col-md-3 col-sm-4">
        <div class="panel panel-default row-space-4">
          <div class="panel-body">
            <div class="media media-photo-block">
              <img alt="${sessionScope._user.username}" title="${sessionScope._user.username}" class="img-responsive"
                <c:choose><c:when test="${not empty sessionScope._user.avatar}">src="http://yikeyun.b0.upaiyun.com/${sessionScope._user.avatar}!M"</c:when><c:otherwise>src="http://img3.imgtn.bdimg.com/it/u=3799866287,2225466980&fm=23&gp=0.jpg"</c:otherwise></c:choose>>
            </div>
            <h2 class="text-center">${sessionScope._user.username}</h2>
          </div>
        </div>
        <div class="panel panel-default row-space-4">
          <div class="panel-heading">帐号信息 </div>
          <div class="panel-body">
            <ul class="list-unstyled">
              <li>
                <div class="alert alert-success">
                  <a href="/courses"><span class="glyphicon glyphicon-user" aria-hidden="true"></span><span>我的课程</span></a>
                </div>
              </li>
             <c:if test="${sessionScope._user.instructor eq true}">
              <li>
                <div class="alert alert-success">
                  <a href="/courses/as_a_student"><span class="glyphicon glyphicon-tasks" aria-hidden="true"></span><span>我报名的课程</span></a>
                </div>
              </li>
              </c:if>
              <li>
                <div class="alert alert-success">
                  <a href="/settings/profile"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span><span>个人资料编辑</span></a>
                </div>
              </li>
              <li>
                <div class="alert alert-success">
                  <a href="/settings/password"><span class="glyphicon glyphicon-transfer" aria-hidden="true"></span><span>密码修改</span></a>
                </div>
              </li>
              <c:if test="${sessionScope._user.admin}">
                <li>
                  <div class="alert alert-danger">
                    <a href="/admin" target="_blank"><span class="glyphicon glyphicon-dashboard" aria-hidden="true">内容管理系统</span></a>
                  </div>
                </li>
              </c:if>
            </ul>
          </div>
        </div>
        <div class="panel panel-default row-space-4">
          <div class="panel-heading">
            <div>验证项</div>
          </div>
          <div class="panel-body">
            <ul class="list-unstyled row row-condensed">
              <li class="col-md-12">
                <div class="media">
                  <c:choose>
                    <c:when test="${1 eq sessionScope._user.status}">
                      <span class="glyphicon glyphicon-ok-circle glyphicon-lg pull-left" aria-hidden="true"></span>
                      <div class="media-body">
                        <div>
                          <c:choose>
                            <c:when test="${not empty sessionScope._user.email}">电子邮件地址</c:when>
                            <c:when test="${not empty sessionScope._user.phone}">手机号码</c:when>
                          </c:choose>
                        </div>
                        <div class="text-muted">已验证</div>
                      </div>
                    </c:when>
                    <c:otherwise>
                      <span class="glyphicon glyphicon-remove-circle glyphicon-lg pull-left" aria-hidden="true"></span>
                      <div class="media-body">
                        <c:choose>
                          <c:when test="${not empty sessionScope._user.email}">电子邮件地址</c:when>
                          <c:when test="${not empty sessionScope._user.phone}">手机号码</c:when>
                        </c:choose>
                        <div class="text-muted">未验证</div>
                      </div>
                    </c:otherwise>
                  </c:choose>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div class="col-md-8 col-sm-8">
        <c:if test="${not _blank}">
          <d:body />
        </c:if>
      </div>
    </div>
  </div>
  <%@ include file="/WEB-INF/commons/footer.jsp"%>
</body>
</html>