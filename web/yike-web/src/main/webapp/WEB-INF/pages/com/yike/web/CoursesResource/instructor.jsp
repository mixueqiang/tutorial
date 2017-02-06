<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div id="courses">
  <div class="panel panel-default row-space-4">
    <div class="panel-heading">我发布的课程</div>
    <div class="panel-body">
      <a class="btn btn-warning" href="/course/create" target="_blank">发布新课程</a>
    </div>
  </div>
  <div id="hint" class="alert" role="alert" style="display: none;">你还没有发布课程！</div>
  <div id="contentList">
    <c:forEach var="item" items="${courses}">
      <div class="panel panel-default" data-id="${item.id}">
        <div class="panel-body">
          <div class="courses-contents row  row-top">
            <div class="courses-imgcover col-sx-8 col-sm-4 col-md-4">
              <c:choose>
                <c:when test="${not empty item.image}">
                  <div class="courses-img" style="width:100%; background: url('http://yikeyun.b0.upaiyun.com/${item.image}!M') 0 0 no-repeat; background-size:cover;"></div>
                </c:when>
                <c:otherwise>
                  <div class="courses-img" style=" width:100%; background: url('http://yikeyun.b0.upaiyun.com/static/course-cover.png!M') 0 0 no-repeat; background-size:cover;"></div>
                </c:otherwise>
              </c:choose>
            </div>
            <div class="courses-details section-right col-md-8 col-sm-7 col-sx-6">
              <div class="row">
                <div class="section-title courses-title col-md-8 col-sm-12 col-xs-12">
                  <a href="/course/${item.id}" target="_blank">${item.name}</a>
                </div>
                <div class="col-md-4 col-sm-12 col-xs-12">
                  <div class="pull-right course-status">
                    <c:choose>
                      <c:when test="${item.status eq 0}">
                        <span>审核中</span>
                        <c:choose>
                          <c:when test="${empty item.countThis}">
                            <a class="btn-link col-space-2 students-list" href=""></a>
                          </c:when>
                          <c:when test="${item.countThis eq 0}">
                            <a class="btn-link col-space-2 students-list" href=""></a>
                          </c:when>
                          <c:otherwise>
                            <a class="btn-link col-space-2 students-list" href="/course/${item.id}/students">学生名单</a>
                          </c:otherwise>
                        </c:choose>
                        <a class="btn-link col-space-2" data-id="${item.id}" href="../course/${item.id}/edit"><span>编辑</span></a>
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
              <div class="section-caption row-space-top-1">
                <a href="/instructor/${item.properties.instructor.id}" target="_blank">${item.properties.instructor.name}</a>
              </div>
              <div class="section-content  row-space-top-1">
                <a href="/course/${item.id}" target="_blank">${item.content}</a>
              </div>
            </div>
            <div class="section-footer courses-footer row-space-top-1">
              <span id="courses-subscript pull-left">${item.subscript}</span> <span class="courses-price pull-right"><c:choose>
                  <c:when test="${item.free eq 1}">免费</c:when>
                  <c:otherwise>¥${item.price}</c:otherwise>
                </c:choose></span>
            </div>
          </div>
          <!-- <div class="as-instructor-content row">
            <div class="as-instructor-imgcover col-md-4 col-sm-4 col-xs-12">
              <c:choose>
                <c:when test="${not empty item.image}">
                  <div class="as-instructor-img" style="width:100%; background: url('http://yikeyun.b0.upaiyun.com/${item.image}!M') 0 0 no-repeat; background-size:cover;"></div>
                </c:when>
                <c:otherwise>
                  <div class="as-instructor-img" style="width: 100%; background: url('http://yikeyun.b0.upaiyun.com/static/course-cover.png!M') 0 0 no-repeat; background-size: cover;"></div>
                </c:otherwise>
              </c:choose>
            </div>
            <div class="as-instructor-details col-md-8 col-sm-8 col-xs-12">
              <div class="row">
                <div class="col-md-8 col-sm-12 col-xs-12">
                  <a class="as-instructor-title" href="/course/${item.id}" target="_blank">${item.name}</a>
                </div>
                <div class="col-md-4 col-sm-12 col-xs-12">
                  <div class="pull-right course-status">
                    <c:choose>
                      <c:when test="${item.status eq 0}">
                        <span>审核中</span>
                        <c:choose>
                          <c:when test="${empty item.countThis}">
                            <a class="btn-link col-space-2 students-list" href=""></a>
                          </c:when>
                          <c:when test="${item.countThis eq 0}">
                            <a class="btn-link col-space-2 students-list" href=""></a>
                          </c:when>
                          <c:otherwise>
                            <a class="btn-link col-space-2 students-list" href="/course/${item.id}/students">学生名单</a>
                          </c:otherwise>
                        </c:choose>
                        <a class="btn-link col-space-2" data-id="${item.id}" href="../course/${item.id}/edit"><span>编辑</span></a>
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
              <div class="as-instructor-lightspot">
                <a href="/course/${item.id}" target="_blank">${item.content}</a>
              </div>
            </div>
            <div class="as-instructor-footer row-space-top-1">
              <span id="courses-subscript pull-left">${item.subscript}</span> <span class="courses-price pull-right"><c:choose>
                  <c:when test="${item.free eq 1}">免费</c:when>
                  <c:otherwise>¥${item.price}</c:otherwise>
                </c:choose></span>
            </div>
          </div> -->
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
	function Heightadapt(){
    /*列表图片成比例*/
    $('.courses-imgcover .courses-img').height($('.courses-imgcover .courses-img').width()*0.60);
    /*下角标位置动态问题*/
    $('.courses-footer').css({
      left:$('.courses-details').position().left,
      width:$('.courses-details').width()+15
    })
  }
  Heightadapt();
  window.onresize=function(){
    Heightadapt();
  }
</script>
