<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<ol class="breadcrumb">
  <li><a href="/admin">Home</a></li>
  <li class="active">Order</li>
</ol>

<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <table class="table table-condensed table-hover" id="orders">
            <thead>
              <tr>
                <th style="width: 30%;">课程</th>
                <th>用户</th>
                <th>报名信息</th>
                <th>报名时间</th>
                <th>进度</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item" items="${orders}">
                <tr data-id="${item.id}">
                  <td><a href="/course/${item.courseId}" target="_blank">${item.properties.course.name}</a></td>
                  <td><a href="/user/${item.userId}" target="_blank">${item.properties.user.username}</a></td>
                  <td>${item.phone}／${item.name}／<c:choose>
                      <c:when test="${not empty item.qq}">${item.qq}</c:when>
                      <c:when test="${not empty item.whchat}">${item.wechat}</c:when>
                    </c:choose></td>
                  <td><jsp:useBean id="createDate" class="java.util.Date" /> <jsp:setProperty name="createDate" property="time" value="${item.createTime}" /> <fmt:formatDate
                      value="${createDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                  <td>${item.progress}</td>
                  <td></td>
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