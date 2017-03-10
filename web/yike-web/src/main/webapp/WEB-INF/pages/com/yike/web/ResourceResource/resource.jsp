<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>${skill.name}学习资料</title>

<div class="container row-space-top-2">
  <div class="row">
    <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 col-xs-8 col-xs-offset-2 card">
      <div class="card-cover">
        <c:if test="${not empty skill.image}">
          <img alt="技能图标" src="http://yikeyun.b0.upaiyun.com${skill.image}!M">
        </c:if>
      </div>
      <div class="card-title">
        <span class="xl">${skill.name}</span>
      </div>
      <div class="card-attribute hide">
        <dl>
          <dt>公司</dt>
          <dd>${skill.companyCount}</dd>
        </dl>
        <dl class="col-space-6">
          <dt>用户</dt>
          <dd>${skill.userCount}</dd>
        </dl>
      </div>
    </div>
  </div>

  <div class="row row-space-top-1 section">
    <div class="col-md-8 col-md-offset-2 col-sm-10 col-sm-offset-1 row-space-top-2">
      <div class="panel panel-default" data-id="${resource.id}">
        <div class="panel-heading">${resource.title}</div>
        <div class="panel-body">${resource.content}</div>
      </div>
    </div>
  </div>
</div>

<script src="/js/resource.js?v=20170228001"></script>