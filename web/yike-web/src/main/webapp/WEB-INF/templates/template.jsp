<!DOCTYPE HTML><%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/commons/head.jsp"%>
<d:head />
</head>
<body>
  <%@ include file="/WEB-INF/commons/header.jsp"%>
  <%@ include file="/WEB-INF/commons/message.jsp"%>
  <div class="container" id="main">
    <c:if test="${not _blank}">
      <d:body />
    </c:if>
  </div>
  <%@ include file="/WEB-INF/commons/footer.jsp"%>
</body>
</html>