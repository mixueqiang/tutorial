$(function() {
  $('#publish-form').validate({
    rules : {
      title : {
        required : true
      },
      content : {
        required : true
      }
    },
    messages : {
      title : {
        required : '请选择内容简介。'
      },
      content : {
        required : '请选择百度网盘或URL地址。'
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
