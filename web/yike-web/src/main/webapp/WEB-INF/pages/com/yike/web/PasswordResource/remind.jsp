<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-1">
      <h2>找回密码</h2>
      <span class="ext-text">请提供注册时使用的邮箱或手机号码。</span>
      <form id="password-remind-form" name="password-remind-form" action="/password/remind" method="post" class="form-horizontal row-space-top-2">
        <div class="form-group row-space-top-2">
          <label for="email" class="col-md-2 col-sm-2 control-label">邮箱</label>
          <div class="col-md-4 col-sm-4">
            <input type="text" id="email" name="email" tabindex="1" class="form-control" />
          </div>
        </div>
        <div class="form-group row-space-top-2">
          <label for="phone" class="col-md-2 col-sm-2 control-label">手机号码</label>
          <div class="col-md-4 col-sm-4">
            <input type="text" id="phone" name="phone" tabindex="2" class="form-control" />
          </div>
        </div>
        <div class="form-group">
          <div class="col-md-4 col-md-offset-2 col-sm-4 col-sm-offset-2">
            <button type="submit" tabindex="3" class="btn btn-default">提交</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="/js/password.js?v=20160927001"></script>