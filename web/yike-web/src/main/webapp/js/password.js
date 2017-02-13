$(function() {
  $('#password-form').validate({
    rules : {
      password : {
        required : true
      },
      newPassword : {
        required : true,
        minlength : 8
      }
    },
    messages : {
      password : {
        required : '请输入当前密码。'
      },
      newPassword : {
        required : '请输入新的密码。',
        minlength : $.format('密码长度必须大于{0}位，建议同时包含字母和数字。')
      }
    },
    submitHandler : function(form) {
      if (!$(form).valid()) {
        $('.error').eq(0).focus();
        $('input', $(form)).val('');
        return false;
      }

      var password = $('#password').val();
      var md5 = $.md5(password);
      password = $.md5(md5 + md5.substring(27));
      $('#password').val(password);

      password = $('#newPassword').val();
      md5 = $.md5(password);
      password = $.md5(md5 + md5.substring(27));
      $('#newPassword').val(password);

      $(form).ajaxSubmit({
        success : function(resp) {
          if (resp && resp.e == 0) {
            Message.info('密码修改成功，请退出重新登录。', false, $('.form-group:last', $(form)));
          } else {
            Message.error('密码修改失败：' + resp.m, false, $('.form-group:last', $(form)));
          }
          $('input', $(form)).val('');
        },
        error : function() {
          $('input', $(form)).val('');
          Message.error('密码修改失败！', false, $('.form-group:last', $(form)));
        }
      });
    }
  });

  $('#password-remind-form').validate({
    rules : {},
    messages : {},
    submitHandler : function(form) {
      if (!$(form).valid()) {
        $('.error').eq(0).focus();
        return false;
      }

      $(form).ajaxSubmit({
        success : function(resp) {
          if (resp && resp.e == 0) {
            if (resp.r == 'email') {
              Message.info('密码重置链接已经发送到您的邮箱，请查收。', false, $('.form-group:last', $(form)));

            } else {
              window.location.href = '/password/reset?phone=' + $('#phone').val();
            }

          } else {
            Message.error('提交失败：' + resp.m, false, $('.form-group:last', $(form)));
          }
        },
        error : function() {
          Message.error('提交失败，请稍后重试。', false, $('.form-group:last', $(form)));
        }
      });
    }
  });

  $('#password-reset-form').validate({
    rules : {
      id : {
        required : true
      },
      securityCode : {
        required : true
      },
      password : {
        required : true,
        minlength : 8
      }
    },
    messages : {
      id : {
        required : '请输入邮箱或手机号。'
      },
      securityCode : {
        required : '请输入安全验证码。'
      },
      password : {
        required : '请输入新的密码。',
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
      password = $.md5(md5 + md5.substring(27));
      $('#password').val(password);

      $(form).ajaxSubmit({
        success : function(resp) {
          if (resp && resp.e == 0) {
            sessionStorage.setItem("isResetPassword",true);
            Message.info('重置密码成功，3秒后将自动跳转到登录页面。', false, $('.form-group:last', $(form)));
            setTimeout(function() {
              window.location.href = '/signin';
            }, 3000);

          } else {
            $('#securityCode').val('');
            $('#password').val('');
            Message.error('重置密码失败：' + resp.m, true, $('.form-group:last', $(form)));
          }
        },
        error : function() {
          $('#securityCode').val('');
          $('#password').val('');
          Message.error('重置密码失败！', true, $('.form-group:last', $(form)));
        }
      });
    }
  });

});
