<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-1">
      <h2>重置密码</h2>
      <form id="password-reset-form" name="password-reset-form" action="/api/v1/password/reset" method="post" class="form-horizontal">
        <div class="panel panel-default ">
          <div class="panel-body">
            <div class="form-group row-space-top-2">
              <label for="id" class="col-sm-3 control-label">帐号</label>
              <div class="col-sm-4">
                <input type="text" id="id" name="id" tabindex="1" class="form-control"
                  <c:choose><c:when test="${not empty param.email}">value="${param.email}"</c:when><c:when test="${not empty param.phone}">value="${param.phone}"</c:when></c:choose>
                  placeholder="请输入邮箱或手机号" />
              </div>
            </div>
            <div class="form-group row-space-top-2">
              <label for="securityCode" class="col-sm-3 control-label">验证码</label>
              <div class="col-sm-4">
                <input type="text" id="securityCode" name="securityCode" tabindex="2" class="form-control" value="${param.authCode}" placeholder="请输入验证码" />
              </div>
            </div>
            <div class="form-group row-space-top-2">
              <label for="password" class="col-sm-3 control-label">新密码</label>
              <div class="col-sm-4">
                <input type="password" id="password" name="password" tabindex="3" class="form-control" placeholder="请输入新密码" />
              </div>
            </div>
            <div class="form-group">
              <div class="col-sm-offset-3 col-sm-4">
                <button type="submit" tabindex="3" class="btn btn-danger">提交</button>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="/libs/jquery/jquery.md5.js"></script>
<script src="/js/password.js?v=20160927001"></script>