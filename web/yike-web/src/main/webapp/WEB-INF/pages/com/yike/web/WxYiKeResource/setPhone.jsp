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
  <title>绑定手机号</title>
</head>
<body>
<div class="container signcontainer">
  <div class="row">
    <div class="col-md-8 col-md-offset-1">
      <form id="set-phone" name="set-phone" action="/api/v1/user" method="post" class="form-horizontal row-space-top-4">
      	<div class="form-group row-space-top-2 hide">
          <label for="oid" class="col-sm-3 control-label"></label>
          <div class="col-sm-4">
            <input type="text" id="oid" name="oid" tabindex="1" class="form-control" value="${oid}"/>
          </div>
        </div>
        <div class="form-group row-space-top-2">
          <label for="phone" class="col-sm-3 control-label">手机号码</label>
          <div class="col-sm-4">
            <input type="text" id="phone" name="phone" tabindex="1" class="form-control" />
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-3 col-sm-offset-3 col-xs-4">
            <img class="captcha-code" src="/api/v1/captcha" onclick="this.src='/api/v1/captcha?v='+new Date()*1"><span class="sm text-muted">点击图片更换一个验证码</span>
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-3 control-label" for="captchaCode">图形验证码</label>
          <div class="col-sm-3 ">
            <input class="form-control" id="captchaCode" name="captchaCode" tabindex="2" type="text" />
          </div>
        </div>
        <div class="form-group">
          <label for="securityCode" class="col-sm-3 control-label">验证码</label>
          <div class="col-sm-2 col-xs-2">
            <input type="text" id="securityCode" name="securityCode" tabindex="3" class="form-control"/>
          </div>
          <div class="col-sm-2 col-xs-2">
            <button class="btn btn-warning btn-send-sms">获取验证码</button>
          </div>
        </div>

        <div class="form-group">
          <div class="col-sm-offset-3 col-sm-6">
            <button type="submit" tabindex="6" class="btn btn-danger">绑定</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="/libs/jquery/jquery.md5.js"></script>
<script>
	var wait = 60;
	function countdown(target) {
	  if (wait == 0) {
	    target.removeAttr('disabled');
	    target.text("获取验证码");
	    wait = 60;

	  } else {
	    target.attr('disabled', 'disabled');
	    target.text("重新发送（" + wait + "）");
	    wait--;
	    setTimeout(function() {
	      countdown(target);
	    }, 1000)
	  }
	}
	function showFormFieldError(target, message) {
	  var html = '<label class="error" for="' + target + '" >' + message + '</label>';
	  $('#' + target).parent().append(html);
	}
	$('.btn-send-sms', $('#set-phone')).click(function() {
	    var $btn = $(this);
	    $btn.attr('disabled', 'disabled');
	    $('label.error', $('#set-phone')).remove();

	    var phone = $('input[name=phone]', $('#set-phone')).val();
	    if (phone == null || phone.length != 11) {
	      $btn.removeAttr('disabled');
	      showFormFieldError('phone', '请输入一个有效的手机号码。');
	      return false;
	    }

	    var captchaCode = $('input[name=captchaCode]', $('#set-phone')).val();
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
	        $('input[name=password]', $('#set-phone')).val('');
	        showFormFieldError('securityCode', resp.m);
	      }
	    });

	    return false;
	});
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
          	alert(resp.e);
            var message = '绑定成功，';
            Message.info(message, false, $('.form-group:last', $(form)));

            //返回值中  r == "y" 则需要跳转到 绑定昵称与密码
            if (resp.r == "y") {
            	setTimeout(function() {
	              window.location.href="/wx/binding/pwd";
	            }, 1500);
            }
            if (resp.r == "n") {
            	//返回值中  r == "n" 不需要跳转
            }
          } else {
            Message.error('绑定失败：', false, $('.form-group:last', $(form)));
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
