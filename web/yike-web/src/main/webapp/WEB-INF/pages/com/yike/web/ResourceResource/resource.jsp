<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>${skill.name}资料&nbsp;|&nbsp;技能</title>

<div class="container row-space-top-2 row-space-4">
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

  <div class="row row-space-top-1 section skill-section">
    <div class="col-md-8 col-md-offset-2 col-sm-10 col-sm-offset-1">
      <div class="card item">
        <div class="title lg">${resource.title}</div>
        <c:if test="${not empty resource.content}">
          <div class="content md">${resource.content}</div>
        </c:if>
        <div class="row-space-top-2 md">
          <ul>
            <li><label>链接：</label><a href="${resource.url}" target="_blank">${resource.url}</a></li>
            <c:if test="${not empty resource.password}">
              <li><label>密码：</label> <c:choose>
                  <c:when test="${not empty _user}">
                    <span>${resource.password}</span>
                  </c:when>
                  <c:otherwise>
                    <a href="/signin?to=/resource/${resource.id}">密码已隐藏，请登录查看</a>
                  </c:otherwise>
                </c:choose></li>
            </c:if>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="/js/resource.js?v=20170228001"></script>