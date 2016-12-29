// Message.
var Message = Message || {
  info : function(msg, auto_close, target, isBefore) {
    if ($('#message').length > 0) {
      $('#message').remove();
    }

    var $target;
    if (target) {
      $target = target;

    } else {
      $target = $('.bs-docs-header');
      if ($target.length == 0) {
        $target = $('#header');
      }
    }

    if (isBefore) {
      $target.before('<div id="message" style="display: none;"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>&nbsp;' + msg + '</div>');
    } else {
      $target.after('<div id="message" style="display: none;"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>&nbsp;' + msg + '</div>');
    }
    $('#message').addClass('alert').addClass('alert-success').slideDown(300);

    if (auto_close) {
      setTimeout(function() {
        $('#message').slideUp(300);
      }, 5000);
    }
  },
  error : function(msg, auto_close, target, isBefore) {
    if ($('#message').length > 0) {
      $('#message').remove();
    }

    var $target;
    if (target) {
      $target = target;

    } else {
      $target = $('.bs-docs-header');
      if ($target.length == 0) {
        $target = $('#header');
      }
    }

    if (isBefore) {
      $target.before('<div id="message" style="display: none;"><span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>&nbsp;' + msg + '</div>');
    } else {
      $target.after('<div id="message" style="display: none;"><span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>&nbsp;' + msg + '</div>');
    }
    $('#message').addClass('alert').addClass('alert-danger').slideDown(300);

    if (auto_close) {
      setTimeout(function() {
        $('#message').slideUp(300);
      }, 5000);
    }
  }
};