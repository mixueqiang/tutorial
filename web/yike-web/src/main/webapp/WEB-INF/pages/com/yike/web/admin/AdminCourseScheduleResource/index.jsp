<%--
  Created by IntelliJ IDEA.
  User: ilakeyc
  Date: 2017/3/3
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/taglibs.jsp" %>
<ol class="breadcrumb">
  <li><a href="/admin">Home</a></li>
  <li class="active">课程表</li>
  <c:if test="${oneCourse eq true}">
    <li><a style="color: green;" href="/admin/schedule">全部</a></li>
  </c:if>
</ol>

<div class="container">
  <div class="row">
    <c:if test="${oneCourse eq true}">
      <div class="container">
        <div class="row">
          <div class="col-md-12">
            <strong>当前课程：${course.name}</strong>
            <a href="#" class="pull-right" style="color: red;">生成新的课程表</a><!--红底白字-->
          </div>
        </div>
      </div>
    </c:if>
    <div class="col-md-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <table class="table table-condensed table-hover" id="orders">
            <thead>
            <tr>
              <th>id</th>
              <c:if test="${oneCourse ne true}">
                <th style="width: 30%;">课程</th>
              </c:if>
              <th>课程开始时间</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${schedules}">
              <tr data-id="${item.id}">
                <td>${item.id}</td>
                <c:if test="${oneCourse ne true}">
                  <td><a href="/admin/schedule?courseId=${item.properties.course.id}">${item.properties.course.name}</a>
                  </td>
                </c:if>
                <td>${item.launchDate} ${item.launchTime}</td>
                <td><a href="#">编辑</a></td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
      <c:if test="${oneCourse eq true}">
        <a href="#" style="color: red; text-align: center;">添加新日程</a>
      </c:if>

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