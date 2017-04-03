$('#courseForm').validate({
  rules : {
    title : {
      required : true,
      maxlength : 30
    },
    content : {
      required : true,
      maxlength : 150
    },
    price : {
      required : true,
      maxlength : 5,
      number : true
    },
    countThis : {
      required : true,
      maxlength : 4,
      digits : true
    },
    description : {
      required : true,
      maxlength : 3000
    },
    teachingType : {
      required : true,
      maxlength : 2000
    }
  },
  messages : {
    title : {
      required : '请输入课程名称。',
      maxlength : $.format("课程名称不能超过 {0} 个字。")
    },
    content : {
      required : '请输入课程亮点。',
      maxlength : $.format("课程亮点不能超过 {0} 个字。")
    },
    price : {
      required : '请输入课程价格。',
      maxlength : $.format("课程价格最多可以输入 {0} 个字符。"),
      number : "请输入有效的数字。",
    },
    countThis : {
      required : '请输入招生人数。',
      maxlength : $.format("招生人数不能超过 {0} 个字。"),
      digits : "只能输入数字",
    },
    description : {
      required : '请输入详细介绍。',
      maxlength : $.format("详细介绍不能超过 {0} 个字。")
    },
    teachingType : {
      required : '请输入课程安排。',
      maxlength : $.format("课程安排不能超过 {0} 个字。")
    }
  },
  submitHandler : function(form) {
    var $btn = $('button[type=submit]', $(form));
    $btn.attr('disabled', 'disabled').addClass('disabled');
    if (!$(form).valid()) {
      $('.error').eq(0).focus();
      $btn.removeAttr('disabled').removeClass('disabled');
      return false;
    }
    $(form).ajaxSubmit({
      success : function(resp) {
        if (resp && resp.e == 0) {
          $('input,textarea', $(form)).val('');
          $btn.removeAttr('disabled').removeClass('disabled');
          var id = resp.r;
          Message.info('课程发布成功。', false, $('.form-group:last', $(form)));
          setTimeout(function() {
            window.location.href = "/courses/as_an_instructor";
          }, 1000);

        } else {
          $btn.removeAttr('disabled').removeClass('disabled');
          Message.error('课程发布失败：' + resp.m, false, $('.form-group:last', $(form)));
        }
      },
      error : function() {
        $btn.removeAttr('disabled').removeClass('disabled');
        Message.error('课程发布失败！', false, $('.form-group:last', $(form)));
      }
    });
  }
});

$('#applicationModal').on('show.bs.modal', function(e) {
  $('.application-title', $('.application-check')).html('正在检查报名信息，请等待...');
  var courseId = $('#courseId').val();
  $.getJSON('/api/v1/course/application/check', {
    courseId : courseId
  }, function(resp) {
    setTimeout(function() {
      if (resp && resp.e == 0) {
        $('.application-check', $('#applicationModal')).hide();
        $('.application-form', $('#applicationModal')).show();

      } else if (resp.e == 10001) {
        $('.loading', $('.application-check')).hide();
        $('.application-title', $('.application-check')).html(resp.m + '<a class="btn-link" href="/signin?to=/course/' + courseId + '">登录</a>');

      } else if (resp.e == 60404) {
        $('.loading', $('.application-check')).hide();
        $('.application-title', $('.application-check')).html(resp.m);

      } else if (resp.e == 60110) {
        $('.loading', $('.application-check')).hide();
        $('.application-title', $('.application-check')).html(resp.m);

      } else if (resp.e == 60111) {
        $('.loading', $('.application-check')).hide();
        $('.application-title', $('.application-check')).html(resp.m + '请到 <a class="btn-link" href="/courses/as_a_student">我的课程</a> 里查看或支付课程。');
      }

    }, 1500);
  });
});

$('#applicationModal').on('show.bs.modal', function(e) {
  $('.application-title', $('.application-check')).html('正在检查报名信息，请等待...');
  $('.loading', $('.application-check')).show();
  $('.application-check', $('#applicationModal')).show();
  $('.application-form', $('#applicationModal')).hide();
  $('.application-pay', $('#applicationModal')).hide();
});

$('#application-form').validate({
  rules : {
    name : {
      required : true,
      maxlength : 10
    },
    phone : {
      required : true,
      maxlength : 20
    },
    qq : {
      required : true
    },
    wechat : {
      required : true
    }
  },
  messages : {
    name : {
      required : '请输入姓名。',
      maxlength : $.format("姓名不能超过 {0} 个字。")
    },
    phone : {
      required : '请输入手机号码。',
      maxlength : $.format("手机号码不能超过 {0} 个字。")
    },
    qq : {
      required : '请输入QQ号码。'
    },
    wechat : {
      required : '请输入微信号。'
    }
  },
  submitHandler : function(form) {
    var $btn = $('button[type=submit]', $(form));
    $btn.attr('disabled', 'disabled').addClass('disabled');
    if (!$(form).valid()) {
      $('.error').eq(0).focus();
      $btn.removeAttr('disabled').removeClass('disabled');
      return false;
    }

    $(form).ajaxSubmit({
      success : function(resp) {
        if (resp && resp.e == 0) {
          $btn.removeAttr('disabled').removeClass('disabled');

          var application = resp.r;
          if (application.progress == 1) {
            Message.info('请支付你的订单...', true, $('.form-group:last', $(form)));

            setTimeout(function() {
              $('.application-form', $('#applicationModal')).hide();
              $('.application-pay', $('#applicationModal')).show();

            }, 1500);

          } else if (application.progress == 100) {
            Message.info('报名成功。', true, $('.form-group:last', $(form)));

            setTimeout(function() {
              window.location = '/courses/as_a_student';

            }, 1500);
          }

        } else {
          $btn.removeAttr('disabled').removeClass('disabled');
          Message.error('报名失败：' + resp.m, false, $('.form-group:last', $(form)));
        }
      },
      error : function() {
        $btn.removeAttr('disabled').removeClass('disabled');
        Message.error('报名失败！', false, $('.form-group:last', $(form)));
      }
    });
  }
});

$('.op-confirm-close').click(function() {
  var courseId = $(this).attr('data-id');
  $('.op-close', $('#confirmCloseModal')).attr('data-id', courseId);
  $('#confirmCloseModal').modal('show');

  return false;
});

$('.op-close').click(function() {
  var courseId = $(this).attr('data-id');
  var $target = $('.op-confirm-close[data-id=' + courseId + ']');
  $.getJSON('/api/v1/course/' + courseId + '/close', {}, function(resp) {
    if (resp && resp.e == 0) {
      $('.course-status', $('.section-course[data-id=' + courseId + ']')).html('<span>已结束招生</span>');
      $('#confirmCloseModal').modal('hide');

    } else {
      Message.error('无法结束招生。', false, $target.parent().parent().parent());
    }
  });

  return false;
});

$('.op-confirm-edit').click(function() {
  var courseId = $(this).attr('data-id');
  $('.op-edit', $('#confirmEditModal')).attr('href', '/course/' + courseId + '/edit');
  $('#confirmEditModal').modal('show');

  return false;
});