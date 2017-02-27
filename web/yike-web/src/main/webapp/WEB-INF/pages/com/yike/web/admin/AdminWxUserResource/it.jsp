<%--
  Created by IntelliJ IDEA.
  User: ilakeyc
  Date: 2017/2/24
  Time: 18:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/taglibs.jsp" %>
<ol class="breadcrumb">
  <li><a href="/admin">Home</a></li>
  <li class="active">IT技能成长联盟公众号-用户</li>
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
              <th>OpenId</th>
              <th>昵称</th>
              <th>性别</th>
              <th>所在地区</th>
              <th>关注时间</th>
              <th>入学状态</th>
              <th>关注状态</th>
              <th>邀请人OpenId</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${users}">
              <tr data-id="${item.id}">
                <td>${item.id}</td>
                <td>${item.openid}</td>
                <td>${item.nickname}</td>
                <td>${item.sex}</td>
                <td>${item.country}-${item.province}-${item.city}</td>
                <td>
                  <jsp:useBean id="createDate" class="java.util.Date"/>
                  <jsp:setProperty name="createDate" property="time" value="${item.createTime}"/>
                  <fmt:formatDate
                      value="${createDate}" pattern="MM-dd HH:mm" timeZone="GMT+0800"/></td>
                <td><c:choose>
                  <c:when test="${1 eq item.isStudent}"><p style="color: green;">已入学</p></c:when>
                  <c:otherwise><p style="color: red;">未入学</p></c:otherwise>
                </c:choose></td>
                <td><c:choose>
                  <c:when test="${1 eq item.subscribe}"><p style="color: green;">关注中</p></c:when>
                  <c:otherwise><p style="color: red;">未关注</p></c:otherwise>
                </c:choose></td>
                <td>${item.inviterId}</td>
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