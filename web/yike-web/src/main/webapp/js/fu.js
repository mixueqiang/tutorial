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

});
