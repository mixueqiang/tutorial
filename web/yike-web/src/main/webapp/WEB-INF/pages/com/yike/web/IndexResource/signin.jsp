<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container signcontainer">
  <div class="row">
    <div class="col-md-8 col-md-offset-1">
      <h2>登录</h2>
      <form id="signin-form" name="signin-form" action="/api/v1/user/signin" method="post" class="form-horizontal row-space-top-4">
        <div class="form-group row-space-top-2">
          <label for="id" class="col-sm-3 control-label">帐号</label>
          <div class="col-sm-4">
            <input type="text" id="id" name="id" tabindex="1" class="form-control" placeholder="手机号码/邮箱" />
          </div>
        </div>
        <div class="form-group">
          <label for="password" class="col-sm-3 control-label">密码</label>
          <div class="col-sm-4">
            <input type="password" id="password" name="password" tabindex="2" class="form-control" />
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-3 col-sm-9">
            <input type="hidden" id="to" name="to" value="${param.to}" />
            <button type="submit" tabindex="3" class="btn btn-danger">登录</button>
            <a class="col-space-2" href="/password/remind">忘记密码？</a>
            <a class="col-space-2 back-signup" href="javascript:;">注册</a>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="/libs/jquery/jquery.md5.js"></script>
<script src="/js/user.js?v=20170104001"></script>
<script>
  //登陆页 跳转到 注册页 时，
  //本地存储一个变量-用来标记-由 登陆页 跳转到注册页
  $(".back-signup").click(function(){
    sessionStorage.setItem("isSignin",true);
    window.location.href="/signup";
  });
</script>