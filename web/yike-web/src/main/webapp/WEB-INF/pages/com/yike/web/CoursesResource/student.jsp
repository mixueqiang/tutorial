<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div id="courses">
  <div class="panel panel-default row-space-4">
    <c:choose>
      <c:when test="${isInstructor eq true}">
        <div class="panel-heading">我报名的课程</div>
      </c:when>
      <c:otherwise>
        <div class="panel-heading">我的课程</div>
      </c:otherwise>
    </c:choose>
    <div class="panel-body">
      <a class="btn btn-warning" href="/course" target="_blank">去选课</a>
    </div>
  </div>
  <div id="hint" class="alert" role="alert" style="display: none;">你当前没有课程!</div>

  <div id="contentList">
    <c:forEach var="item" items="${courses}">
      <div class="panel panel-default section-course" data-id="${item.id}" data-free="${item.free}">
        <div class="panel-body">
          <div class="row  row-top">
            <div class="col-sx-8 col-sm-4 col-4">
              <c:choose>
                <c:when test="${not empty item.image}">
                  <img alt="一课-课程" src="http://yikeyun.b0.upaiyun.com/${item.image}!M">
                </c:when>
                <c:otherwise>
                  <img alt="一课-课程" src="http://yikeyun.b0.upaiyun.com/static/course-cover.png!M">
                </c:otherwise>
              </c:choose>
            </div>
            <div class="col-sx-8 col-sm-8 col-8">
              <div class="row">
                <div class="col-md-8 col-sm-8 col-xs-12">
                  <a class="courses-title" href="/course/${item.id}" target="_blank">${item.name}</a>
                </div>
                <div class="col-md-4 col-sm-5 col-xs-12">
                  <a class="courses-colse op-confirm-cancel" data-id="${item.id}" href="#">取消报名</a>
                </div>
              </div>
              <a class="courses-name" href="/instructor/${item.properties.instructor.id}" target="_blank">${item.properties.instructor.name}</a> <a id="contentStr" class="courses-content"
                href="/course/${item.id}" target="_blank">${item.content}</a>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</div>
<!-- Modal -->
<div class="modal fade" id="cancelModal" tabindex="0" role="dialog" aria-labelledby="cancelModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title" id="cancelModalLabel">取消报名？</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="col-md-2 col-sm-3">
            <div class="icon-lg icon-center icon-danger">
              <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
            </div>
          </div>
          <div class="col-md-10 col-sm-9">
            <div class="row-space-top-1 t-content-medium closeapplyContent">确认要取消报名？</div>
            <div class="row-space-top-3">
              <a class="btn btn-danger op-cancel" href="#">取消报名</a>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>
<div class="modal fade" id="cancelInformationModal" tabindex="0" role="dialog" aria-labelledby="cancelInformationModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title" id="cancelInformationModalLabel">怎样取消报名？</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="col-md-2 col-sm-3">
            <div class="icon-lg icon-center icon-danger">
              <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
            </div>
          </div>
          <div class="col-md-10 col-sm-9">
            <div class="row-space-top-1 t-content-medium closeapplyContent">收费课程怎样取消报名？</div>
            <div class="row-space-top-3">请加QQ群：475581666，或者发送邮件到：service@transkip.com 提供你的账号信息、需要取消的课程链接以及你的支付宝收款账号。</div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>

<script>
  $(function() {
    if ($('#contentList').text().trim()) {
      $('#hint').hide();
    } else {
      $('#hint').show();
    }

    $('.op-confirm-cancel').click(function() {
      var courseId = $(this).attr('data-id');
      var free = $('.section-course[data-id=' + courseId + ']').attr('data-free');

      if (free == 1) {
        $('.op-cancel', $('#cancelModal')).attr('data-id', courseId);
        $('#cancelModal').modal('show');

      } else {
        $('#cancelInformationModal').modal('show');
      }

      return false;
    });

    $(".op-cancel").click(function() {
      var courseId = $(this).attr('data-id');

      $.getJSON("/api/v1/course/application/" + courseId + "/cancel", {}, function(result) {
        if (result && result.e == 0) {
          window.location.reload();

        } else {
          $(".closeapplyContent").html(result.m);
        }
      });

      return false;
    });

  })
</script>