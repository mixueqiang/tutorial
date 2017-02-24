<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/taglibs.jsp" %>
<ol class="breadcrumb">
  <li><a href="/admin">Home</a></li>
  <li class="active">User</li>
</ol>

<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <table class="table table-condensed table-hover" id="users">
            <thead>
            <tr>
              <th style="width: 3%;">ID</th>
              <th>Phone/Email</th>
              <th>昵称</th>
              <th>角色</th>
              <th>注册时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${users}">
              <tr data-id="${item.id}">
                <td>${item.id}</td>
                <td><c:choose>
                  <c:when test="${not empty item.phone}">${item.phone}</c:when>
                  <c:when test="${not empty item.email}">${item.email}</c:when>
                </c:choose></td>
                <td><a href="/user/${item.id}" target="_blank">${item.username}</a></td>
                <td>${item.roles}</td>
                <td>
                  <jsp:useBean id="createDate" class="java.util.Date"/>
                  <jsp:setProperty name="createDate" property="time" value="${item.createTime}"/>
                  <fmt:formatDate
                      value="${createDate}" pattern="MM-dd HH:mm" timeZone="GMT+0800"/></td>
                <td><c:choose>
                  <c:when test="${1 eq item.status}"><p style="color: green;">已验证</p></c:when>
                  <c:otherwise><p style="color: red;">未验证</p></c:otherwise>
                </c:choose></td>
                <td><a class="op-ban" href="#" title="封禁"><span class="glyphicon glyphicon-ban-circle"
                                                                aria-hidden="true"></span></a></td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </div>

      <ul class="pagination">
        <c:if test="${currentPage ne 1}">
          <li><a href="${uriPrefix}1">&laquo;</a></li>
        </c:if>
        <c:forEach var="p" items="${pages}">
          <c:choose>
            <c:when test="${p ne currentPage}">
              <li><a href="${uriPrefix}${p}">${p}</a></li>
            </c:when>
            <c:otherwise>
              <li class="active"><a href="#">${p}</a></li>
            </c:otherwise>
          </c:choose>
        </c:forEach>
        <c:if test="${lastPage > 0 and currentPage ne lastPage}">
          <li><a href="${uriPrefix}${lastPage}">&raquo;</a></li>
        </c:if>
      </ul>
    </div>
  </div>
</div>