<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>${skill.name}</title>

<div class="container row-space-top-2">
  <div class="row">
    <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 col-xs-8 col-xs-offset-2 card">
      <div class="card-cover">
        <c:if test="${not empty skill.image}">
          <img alt="技能图标" src="http://yikeyun.b0.upaiyun.com/${skill.image}!M">
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

  <div class="row row-space-top-2 section skill-section">
    <div class="col-md-2 col-sm-3 col-xs-6">
      <div class="card header">
        <div class="title xl">{谁}有</div>
        <div class="md">TA需要{什么}</div>
        <div class="card-footer">
          <div class="card-action md">
            <span class="glyphicon glyphicon-transfer" aria-hidden="true"></span><span>点击交换</span>
          </div>
        </div>
      </div>
    </div>
    <c:forEach var="item" items="${users}">
      <div class="col-md-2 col-sm-3 col-xs-6">
        <div class="card item">
          <div class="title xl">${item.username}</div>
          <div class="md"></div>
          <div class="card-footer">
            <div class="card-action xxl">
              <a class="green op-exchange" href="#" title="交换"><span class="glyphicon glyphicon-transfer" aria-hidden="true"></span></a>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

</div>

<script src="/js/skill.js?v=20170117001"></script>