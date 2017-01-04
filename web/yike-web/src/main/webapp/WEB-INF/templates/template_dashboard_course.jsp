<!DOCTYPE HTML><%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/commons/head.jsp"%>
<d:head />
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
</head>
<body>
  <a class="sr-only" href="#content">Skip to main content</a>
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
      <div class="col-md-12">
        <c:if test="${not _blank}">
          <d:body />
        </c:if>
      </div>
    </div>
  </div>
  <%@ include file="/WEB-INF/commons/footer.jsp"%>
</body>
</html>