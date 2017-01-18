<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>${skill.name}&nbsp;|&nbsp;一课上手</title>

<div class="container row-space-top-4">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <div class="row">
            <div class="col-md-2 col-sm-3 col-xs-4 card-cover">
              <c:choose>
                <c:when test="${not empty skill.image}">
                  <img alt="一课上手-技能头像" src="http://yikeyun.b0.upaiyun.com/${skill.image}!M">
                </c:when>
                <c:otherwise>
                  <img alt="一课上手-技能头像" src="http://yikeyun.b0.upaiyun.com/static/skill-avatar.png!L">
                </c:otherwise>
              </c:choose>
            </div>
            <div class="col-md-10 col-sm-9 col-xs-8 card-content">
              <div class="card-title">
                <span class="xl">${skill.name}</span><a class="green pull-right" href="#"><span class="glyphicon glyphicon-heart-empty xl" aria-hidden="true"></span><span class="hidden-xs">我正在使用</span></a>
              </div>
              <div class="card-attribute row-space-top-2">

                <dl>
                  <dt>公司</dt>
                  <dd>${skill.companyCount}</dd>
                </dl>
                <dl>
                  <dt>用户</dt>
                  <dd>${skill.userCount}</dd>
                </dl>
                <dl>
                  <dt>文章</dt>
                  <dd>${skill.articleCount}</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="row row-space-top-2 section skill-section">
    <div class="col-md-1 col-sm-2 col-xs-2">
      <div class="card header">
        <div class="title">公司</div>
      </div>
    </div>
    <div class="col-md-11 col-sm-10 col-xs-10">
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
    <div class="col-md-1 col-sm-2 col-xs-2">
      <div class="card header">
        <div class="title">文章</div>
      </div>
    </div>
    <div class="col-md-11 col-sm-10 col-xs-10">
      <div class="row">
        <div class="col-md-3">
          <div class="card">123</div>
        </div>
        <div class="col-md-3">
          <div class="card">456</div>
        </div>
        <div class="col-md-3">
          <div class="card">789</div>
        </div>
        <div class="col-md-3">
          <div class="card">012</div>
        </div>
      </div>
    </div>
  </div>

  <div class="row row-space-top-2 section skill-section">
    <div class="col-md-1 col-sm-2 col-xs-2">
      <div class="card header">
        <div class="title">用户</div>
      </div>
    </div>
    <div class="col-md-11 col-sm-10 col-xs-10">
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