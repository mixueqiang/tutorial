<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>交换${fu.left}&nbsp;|&nbsp;集福</title>

<div class="container row-space-top-4">
  <h2>
    交换<strong>${fu.left}</strong>
  </h2>
  <div class="cards">
    <ul class="card-list">
      <li class="card">
        <div class="card-content">
          <img src="http://yikeyun.b0.upaiyun.com/static/${fu.right}!M">
        </div>
      </li>
      <c:forEach var="item" items="${fus}">
        <li class="card">
          <div class="card-title">${item.username}</div>
          <div class="card-footer">
            <div class="card-action">
              <a class="green xl" href="#"><span class="glyphicon glyphicon-heart-empty" aria-hidden="true"></span></a>
            </div>
          </div>
        </li>
      </c:forEach>
    </ul>
  </div>

  <div class="row">
    <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 col-xs-12">
      <form action="/fu/exchange" class="row-space-top-4" id="donate-form" method="post" name="donate-form">
        <div class="form-group">
          <img class="captcha-code" src="/api/v1/captcha" onclick="this.src='/api/v1/captcha?v='+new Date()*1"><span class="sm text-muted">点击图片更换一个验证码</span>
        </div>
        <div class="form-group">
          <label for="captchaCode">图形验证码</label><input class="form-control" id="captchaCode" name="captchaCode" tabindex="1" type="text" />
        </div>
        <div class="form-group row-space-top-2">
          <label for="phone">手机号码</label><input type="text" id="phone" name="phone" tabindex="2" class="form-control" />
        </div>
        <div class="form-group row-space-top-2">
          <button class="btn btn-warning btn-send-sms">获取验证码</button>
        </div>
        <div class="form-group row-space-top-2">
          <label for="securityCode">验证码</label><input type="text" id="securityCode" name="securityCode" tabindex="3" class="form-control" />
        </div>
      </form>
    </div>
  </div>
</div>

<script src="/js/fu.js?v=20170119001"></script>