<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>技术图谱&nbsp;|&nbsp;一课上手</title>

<div class="container row-space-top-4">
  <div class="cards">
    <ul class="card-list">
      <c:forEach var="item" items="${skills}">
        <li class="card">
          <div class="card-title">
            <a href="/skill/${item.slug}">${item.name}</a>
          </div>
          <div class="card-footer">
            <div class="card-action">
              <a class="green xl" href="#"><span class="glyphicon glyphicon-heart-empty" aria-hidden="true"></span></a>
            </div>
          </div>
        </li>
      </c:forEach>
    </ul>
  </div>
</div>

<script src="/js/skill.js?v=20170117001"></script>