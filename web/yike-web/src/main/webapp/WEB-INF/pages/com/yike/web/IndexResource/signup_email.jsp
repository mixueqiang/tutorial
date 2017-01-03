<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-1">
      <h2>新用户注册</h2>
      <form id="signup-form" name="signup-form" action="/api/v1/user" method="post" class="form-horizontal">
        <div class="panel panel-default">
          <div class="panel-body">
            <div class="form-group row-space-top-2">
              <label for="email" class="col-sm-3 control-label">邮箱</label>
              <div class="col-sm-4">
                <input type="email" id="email" name="email" tabindex="1" class="form-control" />
              </div>
              <div class="col-sm-4 control-txt">
                或<a href="/signup/mobile">使用手机号码注册</a>
              </div>
            </div>
            <div class="form-group">
              <label for="securityCode" class="col-sm-3 control-label">验证码</label>
              <div class="col-sm-4">
                <input type="text" id="securityCode" name="securityCode" tabindex="2" class="form-control" />
              </div>
              <div class="col-sm-2">
                <a class="btn btn-warning btn-send-email" href="#">获取验证码</a>
              </div>
            </div>
            <div class="form-group">
              <label for="password" class="col-sm-3 control-label">密码</label>
              <div class="col-sm-4">
                <input type="password" id="password" name="password" tabindex="3" class="form-control" />
              </div>
            </div>
            <div class="form-group">
              <label for="username" class="col-sm-3 control-label">昵称</label>
              <div class="col-sm-4">
                <input type="text" id="username" name="username" tabindex="4" class="form-control" />
              </div>
            </div>
            <!-- <div class="form-group">
              <label for="password" class="col-sm-3 control-label">我是</label>
              <div class="col-sm-4">
                <select class="form-control" id="role" name="role" tabindex="5">
                  <option value="">请选择用户类型</option>
                  <option value="worker" selected="selected">译员</option>
                  <option value="company">翻译公司／工作室</option>
                  <option value="client">客户</option>
                </select>
              </div>
            </div> -->

            <div class="form-group">
              <div class="col-sm-offset-3 col-sm-6">
                <button type="submit" tabindex="6" class="btn btn-danger">注册</button>
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
<script src="/js/user.js?v=20161125001"></script>