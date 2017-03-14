$(function() {
  $('#publish-form').validate({
    rules : {
      title : {
        required : true
      },
      content : {
        maxlength : 200
      },
      url : {
        required : true
      }
    },
    messages : {
      title : {
        required : '请输入资料名称。'
      },
      content : {
        maxlength : $.format('资料介绍不能超过{0}个字。')
      },
      url : {
        required : '请输入网盘链接或网址。'
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
