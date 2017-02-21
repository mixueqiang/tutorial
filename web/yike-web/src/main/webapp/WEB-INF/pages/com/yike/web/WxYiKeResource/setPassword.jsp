<%--
  Created by IntelliJ IDEA.
  User: ilakeyc
  Date: 2017/2/20
  Time: 17:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>绑定昵称与密码</title>
</head>
<body>
<div class="container signcontainer">
  <div class="row">
    <div class="col-md-8 col-md-offset-1">
      <form id="set-password" name="set-password" action="/api/v1/wx/binding/pwd" method="post" class="form-horizontal row-space-top-4">
      	<div class="form-group row-space-top-2 hide">
          <label for="oid" class="col-sm-3 control-label"></label>
          <div class="col-sm-4">
            <input type="text" id="oid" name="oid" tabindex="1" class="form-control" value="${oid}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="username" class="col-sm-3 control-label">昵称</label>
          <div class="col-sm-4">
            <input type="text" id="username" name="username" tabindex="5" class="form-control" />
          </div>
        </div>
        <div class="form-group">
          <label for="password" class="col-sm-3 control-label">密码</label>
          <div class="col-sm-4">
            <input type="password" id="password" name="password" tabindex="4" class="form-control" />
          </div>
        </div>
        

        <div class="form-group">
          <div class="col-sm-offset-3 col-sm-6">
            <button type="submit" tabindex="6" class="btn btn-danger">提交</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
<script src="/libs/jquery/jquery.md5.js"></script>
<script>
	$('#set-password').validate({
    rules : {
      username : {
        required : true,
        minlength : 2,
        maxlength : 20
      },
      password : {
        required : true,
        minlength : 8
      }
    },
    messages : {
      username : {
        required : '请输入昵称。',
        minlength : $.format('昵称不能少于{0}个字，支持中文、字母、数字和下划线。'),
        maxlength : $.format('昵称不能超过{0}个字，支持中文、字母、数字和下划线。')
      },
      password : {
        required : '请输入密码。',
        minlength : $.format('密码长度必须大于{0}位，建议同时包含字母和数字。')
      }
    },
    submitHandler : function(form) {
      if (!$(form).valid()) {
        $('.error').eq(0).focus();
        return false;
      }

      var password = $('#password').val();
      var md5 = $.md5(password);
      var salt = md5.substring(27);
      password = $.md5(md5 + salt);
      $('#password').val(password);

      $(form).ajaxSubmit({
        success : function(resp) {
          if (resp && resp.e == 0) {
            var role = $('#role').val();
            var message = '绑定成功';
            Message.info(message, false, $('.form-group:last', $(form)));
          } else {
            Message.error('绑定失败：'+resp.m, false, $('.form-group:last', $(form)));
          }
        },
        error : function() {
          Message.error('绑定失败！', false, $('.form-group:last', $(form)));
        }
      });
    }
  });
</script>
</body>
</html>
