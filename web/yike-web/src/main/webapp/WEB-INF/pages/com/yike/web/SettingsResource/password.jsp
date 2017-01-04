<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="panel panel-default">
  <div class="panel-heading">密码修改</div>
  <div class="panel-body">
    <form id="password-form" name="password-form" action="/settings/password" method="post" class="form-horizontal" role="form">
      <div class="form-group">
        <label for="password" class="col-md-2 col-sm-2 control-label">当前密码</label>
        <div class="col-md-4 col-sm-4">
          <input type="password" id="password" name="password" class="form-control" />
        </div>
      </div>
      <div class="form-group">
        <label for="newPassword" class="col-md-2 col-sm-2 control-label">新密码</label>
        <div class="col-md-4 col-sm-4">
          <input type="password" id="newPassword" name="newPassword" class="form-control" />
        </div>
      </div>

      <div class="form-group">
        <div class="col-md-4 col-md-offset-2 col-sm-4 col-sm-offset-2">
          <button type="submit" class="btn btn-danger">提交</button>
        </div>
      </div>
    </form>
  </div>
</div>


<script src="/libs/jquery/jquery.md5.js"></script>
<script src="/js/password.js?v=20160927001"></script>