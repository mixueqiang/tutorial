<!DOCTYPE HTML><%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/commons/head.jsp"%>
<d:head />
</head>
<body>
  <%@ include file="/WEB-INF/commons/header.jsp"%>
  <div class="subnav">
    <div class="container">
      <ul class="subnav-list">
        <li><a href="/dashboard" data-class="dashboard" aria-selected="false" class="subnav-item">控制面板</a></li>
        <li><a href="/courses" data-class="course" aria-selected="false" class="subnav-item">我的课程</a></li>
        <li><a href="/settings/profile" data-class="settings/profile" aria-selected="false" class="subnav-item">设置</a></li>
      </ul>
    </div>
  </div>
  <%@ include file="/WEB-INF/commons/message.jsp"%>

  <div class="container row-space-top-4 row-space-4">
    <div class="row">
      <div class="col-md-3 col-sm-4">
        <div class="panel panel-default row-space-4">
          <div class="panel-body">
            <div class="media media-photo-block">
              <img alt="${sessionScope._user.username}" title="${sessionScope._user.username}" class="img-responsive"
                <c:choose><c:when test="${not empty sessionScope._user.avatar}">src="http://yikeyun.b0.upaiyun.com/${sessionScope._user.avatar}!M"</c:when><c:otherwise>src="http://yikeyun.b0.upaiyun.com/static/user-avatar.png!M"</c:otherwise></c:choose>>
            </div>

            <h2 class="text-center">${sessionScope._user.username}</h2>
            <ul class="list-unstyled text-center hide">
              <li><a href="/user/${sessionScope._user.id}">查看个人资料</a></li>
              <li><a href="/users/edit">编辑个人资料</a></li>
            </ul>
          </div>
        </div>
        <div class="panel panel-default row-space-4">
          <div class="panel-heading">帐号信息</div>
          <div class="panel-body">
            <ul class="list-unstyled">
              <li>
                <div class="alert alert-success">
                  <a href="/user/${sessionScope._user.id}" target="_blank"><span class="glyphicon glyphicon-user" aria-hidden="true">个人主页</span></a>
                </div>
              </li>
              <li>
                <div class="alert alert-success">
                  <c:choose>
                    <c:when test="${sessionScope._user.instructor}">
                      <a href="/goto?type=instructor&userId=${sessionScope._user.id}" target="_blank"><span class="glyphicon glyphicon-book" aria-hidden="true">培训课程</span></a>
                    </c:when>
                    <c:otherwise>
                      我是老师，现在<a class="col-space-1" href="/course/create"><span class="glyphicon glyphicon-plus" aria-hidden="true">添加课程</span></a>
                    </c:otherwise>
                  </c:choose>
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
<script>
  $(function() {
    var pathname = window.location.pathname;
    $('.subnav-list .subnav-item').each(function() {
      if (pathname.indexOf($(this).attr('data-class')) == 1) {
        $(this).attr('aria-selected', true);
      } else {
        $(this).attr('aria-selected', false);
      }
    });
  });
</script>
</html>