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

$(function() {
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

  $('.btn-send-email', $('#signup-form')).click(function() {
    var $btn = $(this);
    $btn.attr('disabled', 'disabled');
    $('.error', $('#signup-form')).remove();

    var email = $('input[name=email]', $('#signup-form')).val();
    if (email == null || email.indexOf('@') < 0) {
      $btn.removeAttr('disabled');
      showFormFieldError('email', '请输入一个有效的邮箱。');
      return false;
    }

    $.getJSON('/api/v1/email/send', {
      email : email,
      captchaCode : 'Da$l10',
      type : 'register'
    }, function(resp) {
      if (resp && resp.e == 0) {
        $btn.text('验证码已发送，请到邮箱里查收。');
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

  $('#signup-form').validate({
    rules : {
      email : {
        required : true,
        email : true
      },
      phone : {
        required : true
      },
      securityCode : {
        required : true
      },
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
      email : {
        required : '请输入邮箱。',
        email : '请输入有效的邮箱。'
      },
      phone : {
        required : '请输入手机号码。'
      },
      securityCode : {
        required : '请输入验证码。'
      },
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
            var message = '注册成功，';
            Message.info(message, false, $('.form-group:last', $(form)));
            setTimeout(function() {
              window.history.go(-1);
            }, 1500);

          } else {
            $('input[name=password]', $('#signup-form')).val('');
            Message.error('注册失败：' + resp.m, false, $('.form-group:last', $(form)));
          }
        },
        error : function() {
          $('input[name=password]', $('#signup-form')).val('');
          Message.error('注册失败！', false, $('.form-group:last', $(form)));
        }
      });
    }
  });

  $('#signin-form').validate({
    rules : {
      id : {
        required : true
      },
      password : {
        required : true
      }
    },
    messages : {
      id : {
        required : '请输入手机号码或邮箱。',
      },
      password : {
        required : '请输入密码。'
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
            Message.info('登录成功！', false, $('.form-group:last', $(form)));
            location.replace(document.referrer);
          } else {
            $('#password').val('');
            Message.error('登录失败：' + resp.m, false, $('.form-group:last', $(form)));
          }
        },
        error : function() {
          $('#password').val('');
          Message.error('登录失败！', false, $('.form-group:last', $(form)));
        }
      });
    }
  });

  $('#profile-form').validate({
    rules : {
      username : {
        required : true,
        minlength : 2,
        maxlength : 20
      },
      gender : {
        minlength : 1,
        maxlength : 1
      },
      profile : {
        maxlength : 100
      }
    },
    messages : {
      username : {
        required : '请输入昵称。',
        minlength : $.format('昵称不能少于{0}个字，支持中文、字母、数字和下划线。'),
        maxlength : $.format('昵称不能超过{0}个字，支持中文、字母、数字和下划线。')
      },
      gender : {
        minlength : '性别信息错误，请重新选择性别。',
        maxlength : '性别信息错误，请重新选择性别。'
      },
      profile : {
        maxlength : $.format('个人简介不能超过{0}个字。')
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
            Message.info('更新成功。', true, $('.form-group:last', $(form)));

          } else {
            Message.error('更新失败：' + resp.m, false, $('.form-group:last', $(form)));
          }
        },
        error : function() {
          Message.error('更新失败！', false, $('.form-group:last', $(form)));
        }
      });
    }
  });

});