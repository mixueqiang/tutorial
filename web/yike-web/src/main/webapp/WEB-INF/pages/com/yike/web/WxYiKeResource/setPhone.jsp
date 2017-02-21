<%--
  Created by IntelliJ IDEA.
  User: ilakeyc
  Date: 2017/2/20
  Time: 17:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>绑定</title>
</head>
<body>
<div class="container signcontainer">
  <div class="row">
    <div class="col-md-8 col-md-offset-1">
      <h2>绑定</h2>
      <form id="set-phone" name="set-phone" action="/api/v1/user" method="post" class="form-horizontal row-space-top-4">
        <div class="form-group row-space-top-2">
          <label for="phone" class="col-sm-3 control-label">手机号码</label>
          <div class="col-sm-4">
            <input type="text" id="phone" name="phone" tabindex="1" class="form-control" />
          </div>
        </div>
        <div class="form-group">
          <label for="securityCode" class="col-sm-3 control-label">验证码</label>
          <div class="col-sm-3">
            <input type="text" id="securityCode" name="securityCode" tabindex="3" class="form-control" />
          </div>
          <div class="col-sm-2">
            <button class="btn btn-warning btn-send-sms">获取验证码</button>
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
	 $('#set-phone').validate({
    rules : {
      phone : {
        required : true
      },
      securityCode : {
        required : true
      }
    },
    messages : {
      phone : {
        required : '请输入手机号码。'
      },
      securityCode : {
        required : '请输入验证码。'
      }
    },
    submitHandler : function(form) {
      if (!$(form).valid()) {
        $('.error').eq(0).focus();
        return false;
      }

      $(form).ajaxSubmit({
        success : function(resp) {
          if (resp && resp.e == 0) {
            var role = $('#role').val();
            var message = '绑定成功，';
            sessionStorage.setItem("isSignup",true);
            Message.info(message, false, $('.form-group:last', $(form)));
            setTimeout(function() {
              window.history.go(-1);
            }, 1500);

          } else {
            Message.error('绑定失败：' + resp.m, false, $('.form-group:last', $(form)));
          }
        },
        error : function() {
          Message.error('绑定失败！', false, $('.form-group:last', $(form)));
        }
      });
    }
  });

	 $('.btn-send-sms', $('#signup-form')).click(function() {
	    var $btn = $(this);
	    $btn.attr('disabled', 'disabled');
	    $('label.error', $('#signup-form')).remove();

	    var phone = $('input[name=phone]', $('#signup-form')).val();
	    if (phone == null || phone.length != 11) {
	      $btn.removeAttr('disabled');
	      showFormFieldError('phone', '请输入一个有效的手机号码。');
	      return false;
	    }

	    var captchaCode = $('input[name=captchaCode]', $('#signup-form')).val();
	    if (captchaCode == null || captchaCode.length == '') {
	      $btn.removeAttr('disabled');
	      showFormFieldError('captchaCode', '请输入图形验证码。');
	      return false;
	    }

	    $.getJSON('/api/v1/sms/send', {
	      phone : phone,
	      captchaCode : captchaCode,
	      type : 'register'
	    }, function(resp) {
	      if (resp && resp.e == 0) {
	        $btn.text('验证码已发送，请在手机上查收。');
	        setTimeout(function() {
	          countdown($btn);
	        }, 200);

	      } else {
	        $btn.removeAttr('disabled');
	        $('input[name=password]', $('#signup-form')).val('');
	        showFormFieldError('securityCode', resp.m);
	      }
	    });

	    return false;
	  });
</script>
</body>
</html>
