<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>集福</title>

<div class="container row-space-top-4">
  <div class="row">
    <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 col-xs-8 col-xs-offset-2 card">
      <div class="card-cover">
        <c:if test="${not empty skill.image}">
          <img alt="集福" src="http://yikeyun.b0.upaiyun.com/static/fu1.png!M">
        </c:if>
      </div>
      <div class="card-title">
        <span class="xl">集福</span>
      </div>
      <div class="card-attribute">
        <dl>
          <dt>人</dt>
          <dd>${userCount}</dd>
        </dl>
        <dl class="col-space-6">
          <dt>福</dt>
          <dd>${fuCount}</dd>
        </dl>
      </div>
    </div>
  </div>

  <div class="row row-space-top-2 row-space-6">
    <div class="col-md-8 col-md-offset-2 col-sm-12">
      <div class="row">
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu1">
            <div class="panel-body">
              <a href="/skill/fu_aiguo"><img src="http://yikeyun.b0.upaiyun.com/static/fu1.png!M"></a>
              <h3 class="panel-title row-space-top-2">爱国福</h3>
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_aiguo">我需要</a>
            </div>
          </div>
        </div>
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu2">
            <div class="panel-body">
              <a href="/skill/fu_fuqiang"><img src="http://yikeyun.b0.upaiyun.com/static/fu2.png!M"></a>
              <h3 class="panel-title row-space-top-2">富强福</h3>
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_fuqiang">我需要</a>
            </div>
          </div>
        </div>
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu3">
            <div class="panel-body">
              <a href="/skill/fu_hexie"><img src="http://yikeyun.b0.upaiyun.com/static/fu3.png!M"></a>
              <h3 class="panel-title row-space-top-2">和谐福</h3>
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_hexie">我需要</a>
            </div>
          </div>
        </div>
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu4">
            <div class="panel-body">
              <a href="/skill/fu_youshan"><img src="http://yikeyun.b0.upaiyun.com/static/fu4.png!M"></a>
              <h3 class="panel-title row-space-top-2">友善福</h3>
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_youshan">我需要</a>
            </div>
          </div>
        </div>
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu5">
            <div class="panel-body">
              <a href="/skill/fu_jingye"><img src="http://yikeyun.b0.upaiyun.com/static/fu5.png!M"></a>
              <h3 class="panel-title row-space-top-2">敬业福</h3>
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_jingye">我需要</a>
            </div>
          </div>
        </div>
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu5">
            <div class="panel-body">
              <img src="http://yikeyun.b0.upaiyun.com/static/20170122001.jpg!M">
              <h3 class="panel-title row-space-top-2">关注一课上手</h3>
            </div>
            <div class="panel-footer">
              <a class="btn btn-danger btn-block" href="http://mp.weixin.qq.com/s/TCLtZ5LaUH7R3hcsdAURJQ">投个票吧！</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>