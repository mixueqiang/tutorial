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
  $('#publish-form').validate({
    rules : {
      source : {
        required : true
      },
      target : {
        required : true
      },
      alipay : {
        required : true,
        minlength : 5
      }
    },
    messages : {
      source : {
        required : '请选择你有什么福。'
      },
      target : {
        required : '请选择你需要什么福。'
      },
      alipay : {
        required : '请输入支付宝账号。',
        minlength : '请输入支付宝账号。'
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
            Message.info('提交成功。', false, $('.form-group:last', $(form)));
            setTimeout(function() {
              $('#PublishModal').modal('hide');
            }, 1500);

          } else {
            Message.error('提交失败：' + resp.m, false, $('.form-group:last', $(form)));
          }
        },
        error : function() {
          Message.error('提交失败！', false, $('.form-group:last', $(form)));
        }
      });
    }
  });

  $('.op-exchange').click(function() {
    $('#ExchangeModal').modal('show');

    var $exchange = $(this).parent().parent().parent();
    var exchangeId = $exchange.attr('data-id');

    $.getJSON('/fu/exchange', {
      id : exchangeId
    }, function(resp) {
      setTimeout(function() {
        if (resp && resp.e == 0) {
          var exchange = resp.r;
          var html = '<label>支付宝账号：</label><span>' + exchange.alipay + '</span>';
          $('.alipay-info', $('.exchange-info')).html(html);
          $('.exchange-check', $('#ExchangeModal')).hide();
          $('.exchange-info', $('#ExchangeModal')).show();

        } else if (resp.e == 10001) {
          $('input[name=exchangeId]', $('.exchange-signup')).val(exchangeId);
          $('.exchange-check', $('#ExchangeModal')).hide();
          $('.exchange-signup', $('#ExchangeModal')).show();

        } else {
          var html = '<label class="error">访问失败！</label>';
          $('.alipay-info', $('.exchange-info')).html(html);
          $('.exchange-check', $('#ExchangeModal')).hide();
          $('.exchange-info', $('#ExchangeModal')).show();
        }
      }, 1500);
    });

    return false;
  });

  $('#ExchangeModal').on('show.bs.modal', function(e) {
    $('.alipay-info', $('.exchange-info')).html('');
    $('.exchange-check', $('#ExchangeModal')).show();
    $('.exchange-signup', $('#ExchangeModal')).hide();
    $('.exchange-info', $('#ExchangeModal')).hide();
  });

  $('.btn-send-sms', $('#signup-form')).click(function() {
    var $btn = $(this);
    $btn.attr('disabled', 'disabled');
    $('label.error', $('#signup-form')).remove();

    var phone = $('input[name=phone]', $('#signup-form')).val();
    if (phone == null || phone.length != 11) {
      $btn.removeAttr('disabled');
      showFormFieldError('phone', '请输入有效的手机号。');
      return false;
    }

    $.getJSON('/fu/sms/send', {
      phone : phone,
      type : 'fu'
    }, function(resp) {
      if (resp && resp.e == 0) {
        $btn.text('已发送');
        setTimeout(function() {
          countdown($btn);
        }, 500);

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
      phone : {
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
      phone : {
        required : '请输入手机号码。'
      },
      securityCode : {
        required : '请输入验证码。'
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
            var exchange = resp.r;
            var html = '<label>支付宝账号：</label><span>' + exchange.alipay + '</span>';
            $('.alipay-info', $('.exchange-info')).html(html);
            $('.exchange-check', $('#ExchangeModal')).hide();
            $('.exchange-info', $('#ExchangeModal')).show();

          } else {
            $('input[name=password]', $('#signup-form')).val('');
            Message.error('访问失败：' + resp.m, false, $('.form-group:last', $(form)));
          }
        },
        error : function() {
          $('input[name=password]', $('#signup-form')).val('');
          Message.error('访问失败！', false, $('.form-group:last', $(form)));
        }
      });
    }
  });

});
