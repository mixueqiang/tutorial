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
      <div class="card-attribute lg">有${count}人在使用</div>
    </div>
  </div>

  <div class="row  row-space-top-2">
    <div class="col-md-8 col-md-offset-2 col-sm-12">
      <div class="row">
        <div class="col-sm-4 col-xs-12">
          <div class="panel panel-default" id="fu1">
            <div class="panel-body">
              <img src="http://yikeyun.b0.upaiyun.com/static/fu1.png!M">
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
              <img src="http://yikeyun.b0.upaiyun.com/static/fu2.png!M">
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
              <img src="http://yikeyun.b0.upaiyun.com/static/fu3.png!M">
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
              <img src="http://yikeyun.b0.upaiyun.com/static/fu4.png!M">
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
              <img src="http://yikeyun.b0.upaiyun.com/static/fu5.png!M">
              <h3 class="panel-title row-space-top-2">敬业福</h3>
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_jingye">我需要</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>