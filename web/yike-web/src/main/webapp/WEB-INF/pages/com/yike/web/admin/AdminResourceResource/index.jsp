<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<ol class="breadcrumb">
  <li><a href="/admin">Home</a></li>
  <li class="active">Resource</li>
</ol>

<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <table class="table table-condensed table-hover" id="resources">
            <thead>
              <tr>
                <th style="width: 30%;">资源</th>
                <th>作者</th>
                <th>链接</th>
                <th>密码</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item" items="${resources}">
                <tr data-id="${item.id}">
                  <td><a href="/resource/${item.id}" target="_blank">${item.title}</a></td>
                  <td><a href="/user/${item.userId}" target="_blank">${item.properties.user.name}</a></td>
                  <td><a href="${item.url}" target="_blank">${item.url}</a></td>
                  <td>${item.password}</td>
                  <td>${item.status}</td>
                  <td><a href="#">更新</a></td>
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