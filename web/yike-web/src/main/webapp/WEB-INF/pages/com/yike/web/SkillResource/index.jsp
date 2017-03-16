<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>技能</title>

<div class="container row-space-top-2 row-space-4">
  <div class="row section skill-section">
    <c:forEach var="item" items="${skills}">
      <div class="col-md-2 col-sm-3 col-xs-6 row-space-top-2" data-id="${item.id}">
        <a class="green" href="/skill/${item.slug}" target="_blank">
          <div class="card item">
            <div class="title md">${item.name}</div>
            <div class="card-footer">
              <div class="card-action sm">
                <span>已有 ${item.resourceCount} 份资料</span>
              </div>
            </div>
          </div>
        </a>
      </div>
    </c:forEach>
  </div>
</div>

<script src="/js/skill.js?v=20170117001"></script>