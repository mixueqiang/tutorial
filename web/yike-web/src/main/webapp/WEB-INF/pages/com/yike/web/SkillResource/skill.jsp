<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>${skill.name}&nbsp;|&nbsp;一课上手</title>

<div class="container row-space-top-4">
  <div class="row">
    <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 col-xs-8 col-xs-offset-2 card">
      <div class="card-cover">
        <c:if test="${not empty skill.image}">
          <img alt="一课上手-技能头像" src="http://yikeyun.b0.upaiyun.com/${skill.image}!M">
        </c:if>
      </div>
      <div class="card-title">
        <span class="xl">${skill.name}</span>
      </div>
      <div class="card-attribute">
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

  <div class="row row-space-top-4 section skill-section">
    <div class="col-md-1 col-md-offset-2 col-sm-2 col-sm-offset-1 col-xs-2">
      <div class="card header">
        <div class="title">公司</div>
      </div>
    </div>
    <div class="col-md-7 col-sm-9 col-xs-10">
      <div class="row">
        <div class="col-md-2">
          <div class="card">123</div>
        </div>
        <div class="col-md-2">
          <div class="card">456</div>
        </div>
        <div class="col-md-2">
          <div class="card">789</div>
        </div>
        <div class="col-md-2">
          <div class="card">012</div>
        </div>
      </div>
    </div>
  </div>

  <div class="row row-space-top-2 section skill-section">
    <div class="col-md-1 col-md-offset-2 col-sm-2 col-sm-offset-1 col-xs-2">
      <div class="card header">
        <div class="title">用户</div>
      </div>
    </div>
    <div class="col-md-7 col-sm-9 col-xs-10">
      <div class="row">
        <div class="col-md-2">
          <div class="card">123</div>
        </div>
        <div class="col-md-2">
          <div class="card">456</div>
        </div>
        <div class="col-md-2">
          <div class="card">789</div>
        </div>
        <div class="col-md-2">
          <div class="card">012</div>
        </div>
      </div>
    </div>
  </div>

</div>

<script src="/js/skill.js?v=20170117001"></script>