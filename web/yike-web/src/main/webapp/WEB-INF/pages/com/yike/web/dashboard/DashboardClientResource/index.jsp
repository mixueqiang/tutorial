<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<h3>项目列表</h3>
<table class="table table-condensed table-hover" id="projects">
  <thead>
    <tr>
      <th style="width: 50%;">工作名称</th>
      <th>城市</th>
      <th>语言</th>
      <th>工作行业</th>
      <th>发布时间</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="item" items="${projects}">
      <tr data-id="${item.id}">
        <td><a href="/job/${item.id}" target="_blank">${item.name}</a></td>
        <td>${item.city.name}</td>
        <td>${item.language.nameLocale}</td>
        <td>${item.category}</td>
        <td><jsp:useBean id="postDate" class="java.util.Date" /> <jsp:setProperty name="postDate" property="time" value="${item.createTime}" /> <fmt:formatDate value="${postDate}"
            pattern="yyyy-MM-dd HH:mm" timeZone="GMT+0800" /></td>
      </tr>
    </c:forEach>
  </tbody>
</table>