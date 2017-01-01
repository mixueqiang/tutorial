<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<style>
.courses-content {
  display: block; padding-bottom: 20px; margin-bottom: 30px;
}
</style>
<div class="col-md-3 col-sm-3 col-xs-12">
  <div class="panel panel-default">
    <div class="panel-body">
      <div class="row  row-top">
        <div class="col-sm-12 col-xs-4">
          <c:choose>
            <c:when test="${not empty instructor.avatar}">
              <img alt="翻译资源网-用户头像" src="http://transkip.b0.upaiyun.com/${instructor.avatar}!M">
            </c:when>
            <c:otherwise>
              <img alt="翻译资源网-用户头像" src="http://transkip.b0.upaiyun.com/201612/user-avatar.png!M">
            </c:otherwise>
          </c:choose>
        </div>
        <div class="col-sm-12 col-xs-5">
          <h3 class="text-center">${instructor.name}</h3>
        </div>
        <div class="col-sm-12 col-xs-5">
          <p>${instructor.profile}</p>
        </div>
        <div class="col-sm-12 col-xs-5">
          <a class="btn btn-warning" href="/courses/as_a_student" target="_blank">我报名的课程</a>
        </div>
      </div>
    </div>
  </div>
</div>

<div class="col-md-9 col-sm-9 col-xs-12" id="courses">
  <div class="panel panel-default row-space-4">
    <div class="panel-heading">我的课程</div>
    <div class="panel-body">
      <a class="btn btn-warning" href="/course/create" target="_blank">发布新课程</a>
    </div>
  </div>
  <div id="hint" class="alert" role="alert" style="display: none;">你当前没有课程！</div>
  <div id="contentList">
    <c:forEach var="item" items="${courses}">
      <div class="panel panel-default section-course" data-id="${item.id}">
        <div class="panel-body">
          <div class="row">
            <div class="col-md-4 col-sm-4 col-xs-8">
              <c:choose>
                <c:when test="${not empty item.image}">
                  <img alt="翻译资源网-课程" src="http://transkip.b0.upaiyun.com/${item.image}!M">
                </c:when>
                <c:otherwise>
                  <img alt="翻译资源网-课程" src="http://transkip.b0.upaiyun.com/201612/course-cover.png!M">
                </c:otherwise>
              </c:choose>
            </div>
            <div class="col-md-8 col-sm-8 col-xs-8">
              <div class="row">
                <div class="col-md-8 col-sm-12 col-xs-12">
                  <a class="courses-title" href="/course/${item.id}" target="_blank">${item.name}</a>
                </div>
                <div class="col-md-4 col-sm-12 col-xs-12">
                  <div class="pull-right">
                    <c:choose>
                      <c:when test="${item.status eq 0}">
                        <span>审核中</span>
                        <c:choose>
                          <c:when test="${empty item.currentLearnerCount}">
                            <a class="btn-link col-space-2 students-list" href=""></a>
                          </c:when>
                          <c:when test="${item.currentLearnerCount eq 0}">
                            <a class="btn-link col-space-2 students-list" href=""></a>
                          </c:when>
                          <c:otherwise>
                            <a class="btn-link col-space-2 students-list" href="/course/${item.id}/students">学生名单</a>
                          </c:otherwise>
                        </c:choose>
                        <a class="btn-link col-space-2 op-confirm-edit" data-id="${item.id}" href="#"><span>编辑</span></a>
                      </c:when>
                      <c:when test="${item.status gt 0}">
                        <c:choose>
                          <c:when test="${item.appliable eq 1}">
                            <a class="btn-link op-confirm-close" data-id="${item.id}" href="#"><span>结束招生</span></a>
                            <a class="btn-link col-space-2 op-confirm-edit" data-id="${item.id}" href="#"><span>编辑</span></a>
                          </c:when>
                          <c:otherwise>
                            <span>已结束招生</span>
                          </c:otherwise>
                        </c:choose>
                      </c:when>
                    </c:choose>
                  </div>
                </div>
              </div>
              <a class="courses-content" href="/course/${item.id}" target="_blank">${item.content}</a>
              <div class="courses-hint row-space-top-1">
                <span id="courses-subscript">${item.subscript}</span> <span class="courses-price"><c:choose>
                    <c:when test="${item.free eq 1}">免费</c:when>
                    <c:otherwise>¥${item.price}</c:otherwise>
                  </c:choose></span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</div>

<div class="modal fade" id="confirmCloseModal" tabindex="0" role="dialog" aria-labelledby="confirmCloseModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title" id="confirmCloseModalLabel">确认结束招生？</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="col-md-2 col-sm-3">
            <div class="icon-lg icon-center icon-danger">
              <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
            </div>
          </div>
          <div class="col-md-10 col-sm-9">
            <div class="application-title t-content-medium">结束招生后将无法编辑课程，并且无法重新招生。</div>
            <div class="application-tip row-space-top-1">是否继续结束招生？</div>
            <div class="row-space-top-3">
              <a class="btn btn-danger op-close" href="#">结束招生</a>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      </div>
    </div>
  </div>
</div>
<div class="modal fade" id="confirmEditModal" tabindex="0" role="dialog" aria-labelledby="confirmEditModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title" id="confirmEditModalLabel">确认编辑？</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="col-md-2 col-sm-3">
            <div class="icon-lg icon-center icon-danger">
              <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
            </div>
          </div>
          <div class="col-md-10 col-sm-9">
            <div class="application-title t-content-medium">课程在编辑后需要重新进入审核，审核通过前将无法招生。</div>
            <div class="application-tip row-space-top-1">是否继续编辑？</div>
            <div class="row-space-top-3">
              <a class="btn btn-danger op-edit" href="">继续编辑</a>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      </div>
    </div>
  </div>
</div>

<script src="/js/course.js?v=20161220006"></script>
<script>
  $(function() {
	  /* 没有课程时->显示当前没有课程 */
    if ($('#contentList').text().trim()) {
      $('#hint').hide();
    } else {
      $('#hint').show();
    }
    
  })
</script>