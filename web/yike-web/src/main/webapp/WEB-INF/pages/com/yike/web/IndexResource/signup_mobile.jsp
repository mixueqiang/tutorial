<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-1">
      <h2>新用户注册</h2>
      <form id="signup-form" name="signup-form" action="/api/v1/user" method="post" class="form-horizontal row-space-top-1">
        <div class="panel panel-default">
          <div class="panel-body">
            <div class="form-group row-space-top-2">
              <label for="phone" class="col-sm-3 control-label">手机号码</label>
              <div class="col-sm-4">
                <input type="text" id="phone" name="phone" tabindex="1" class="form-control" />
              </div>
              <div class="col-sm-4 control-txt">
                或<a href="/signup/email">使用邮箱注册</a>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-3 control-label" for="captchaCode">图形验证码</label>
              <div class="col-sm-3">
                <input class="form-control" id="captchaCode" name="captchaCode" tabindex="2" type="text" />
              </div>
              <div class="col-sm-2">
                <img src="/api/v1/captcha" onclick="this.src='/api/v1/captcha?v='+new Date()*1">
              </div>
            </div>
            <div class="form-group">
              <label for="securityCode" class="col-sm-3 control-label">验证码</label>
              <div class="col-sm-3">
                <input type="text" id="securityCode" name="securityCode" tabindex="3" class="form-control" />
              </div>
              <div class="col-sm-2">
                <a class="btn orange btn-send-sms" href="#">获取验证码</a>
              </div>
            </div>
            <div class="form-group">
              <label for="password" class="col-sm-3 control-label">密码</label>
              <div class="col-sm-4">
                <input type="password" id="password" name="password" tabindex="4" class="form-control" />
              </div>
            </div>
            <div class="form-group">
              <label for="username" class="col-sm-3 control-label">昵称</label>
              <div class="col-sm-4">
                <input type="text" id="username" name="username" tabindex="5" class="form-control" />
              </div>
            </div>

            <div class="form-group">
              <div class="col-sm-offset-3 col-sm-6">
                <button type="submit" tabindex="6" class="btn red">注册</button>
                <a class="col-space-2" href="/signin">返回登录</a>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="/libs/jquery/jquery.md5.js"></script>
<script src="/js/user.js?v=20170104001"></script>