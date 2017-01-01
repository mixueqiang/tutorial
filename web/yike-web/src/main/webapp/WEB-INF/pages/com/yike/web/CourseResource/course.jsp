<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>${course.name}&nbsp;|&nbsp;一课</title>

<div class="course" data-id="${course.id}">
  <input type="hidden" id="courseId" value="${course.id}">
  <div class="container">
    <div class="row row-space-top-2 section">
      <div class="col-md-9 col-sm-12" data-purpose="introduction">

        <div class="row section">
          <div class="col-md-6 col-sm-6">
            <c:choose>
              <c:when test="${not empty course.image}">
                <img alt="翻译资源网-实践课图片" src="http://transkip.b0.upaiyun.com/${course.image}!L">
              </c:when>
              <c:otherwise>
                <img alt="翻译资源网-实践课图片" src="http://transkip.b0.upaiyun.com/201612/course-cover.png!L">
              </c:otherwise>
            </c:choose>
          </div>
          <div class="col-md-6 col-sm-6">
            <div class="section-title xl">${course.name}</div>
            <div class="section-content lh-lg row-space-top-2">${course.content}</div>
            <div class="row row-space-top-3 section">
              <div class="col-md-9 col-md-offset-3 col-sm-10 col-xs-offset-2 col-xs-6 col-xs-offset-6">
                <div class="section">
                  <div class="actions row-space-top-2">
                    <span class="course-price lg"><c:choose>
                        <c:when test="${course.free eq 1}">免费</c:when>
                        <c:otherwise>¥${course.price}</c:otherwise>
                      </c:choose></span>
                    <c:choose>
                      <c:when test="${hasApplied}">
                        <button class="btn green large" disabled="disabled">已报名</button>
                      </c:when>
                      <c:otherwise>
                        <c:choose>
                          <c:when test="${course.status eq 0}">
                            <button class="btn gray large" disabled="disabled">审核中</button>
                          </c:when>
                          <c:when test="${course.status eq 1 and course.appliable lt 1}">
                            <button class="btn red large" disabled="disabled">已结束招生</button>
                          </c:when>
                          <c:when test="${course.status eq 1 and course.appliable eq 1}">
                            <a class="btn green large" data-toggle="modal" data-target="#applicationModal" href="#">报名</a>
                          </c:when>
                        </c:choose>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>

      <div class="col-md-2 col-md-offset-1 col-sm-6 col-xs-6">
        <div class="section">
          <div class="img-responsive">
            <a href="/instructor/${instructor.id}"><c:choose>
                <c:when test="${not empty instructor.avatar}">
                  <img alt="翻译资源网-用户头像" src="http://transkip.b0.upaiyun.com/${instructor.avatar}!M">
                </c:when>
                <c:otherwise>
                  <img alt="翻译资源网-用户头像" src="http://transkip.b0.upaiyun.com/201612/user-avatar.png!M">
                </c:otherwise>
              </c:choose></a>
          </div>
          <div class="column-content">
            <div class="section-title lg">
              <a href="/instructor/${instructor.id}">${instructor.name}</a>
            </div>
            <div class="section-content row-space-top-1">${instructor.profile}</div>
          </div>
        </div>
      </div>

    </div>
  </div>

  <div class="container course-navbar row-space-top-4">
    <div class="row row-space-top-3">
      <div class="col-md-12">
        <ul class="nav course-nav">
          <li><a href="#about-the-course">关于此课程</a></li>
          <li><a href="#course-list">课程安排</a></li>
          <c:if test="${not empty achievements}">
            <li><a href="#course-achievements">课程成果</a></li>
          </c:if>
        </ul>
      </div>
    </div>
  </div>

  <div class="course-details">
    <div class="container">
      <div class="row-space-top-3" id="about-the-course">
        <h2>关于此课程</h2>
      </div>
      <div class="row row-space-top-2 section">
        <div class="col-md-8">
          <h4 class="row-space-2">课程介绍</h4>
          <p>${course.description}</p>
          <h4 class="row-space-top-2 row-space-2">课程亮点</h4>
          <p>${course.content}</p>
          <c:if test="${course.countMax gt 0}">
            <h4 class="row-space-top-2 row-space-2">招收学员数量：${course.countMax} 人</h4>
          </c:if>
          <h4 class="row-space-top-2 row-space-2">购买支持</h4>
          <p>翻译资源网对课程的报名及购买提供支持。如对课程或者课程的购买有疑问，请加QQ群：475581666，或者发送邮件到：service@transkip.com 进行咨询。</p>
        </div>
        <div class="col-md-4"></div>
      </div>
    </div>
    <div class="container">
      <div class="row-space-top-3" id="course-list">
        <h2>课程安排</h2>
      </div>
      <div class="row row-space-top-2 section">
        <div class="col-md-8">${course.teachingType}</div>
        <div class="col-md-4"></div>
      </div>
    </div>
    <c:if test="${not empty achievements}">
      <div class="container">
        <div class="row-space-top-3" id="course-achievements">
          <h2>课程成果</h2>
        </div>
        <div class="row row-space-top-2 section">
          <div class="col-md-8"></div>
          <div class="col-md-4"></div>
        </div>
      </div>
    </c:if>
  </div>
</div>

<div class="modal fade" id="applicationModal" tabindex="-1" role="dialog" aria-labelledby="applicationModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title" id="applicationModalLabel">报名</h4>
      </div>
      <div class="application-check">
        <div class="modal-body">
          <div class="row">
            <div class="col-md-12">
              <div class="application-title t-content-medium"></div>
            </div>
          </div>
          <div class="row loading">
            <div class="col-md-3 col-md-offset-4 col-sm-4 col-sm-offset-4 col-xs-6 col-xs-offset-3">
              <img src="/images/loading.gif">
            </div>
          </div>
        </div>
      </div>
      <div class="application-form" style="display: none;">
        <form id="application-form" name="application-form" action="/api/v1/course/application" method="post" class="form-horizontal" role="form">
          <div class="modal-body">
            <div class="form-group">
              <div class="col-md-12">
                <div class="application-title t-content-medium">请确认你的联系信息。确认无误后，提交报名。</div>
              </div>
            </div>
            <div class="form-group">
              <label for="name" class="col-md-2 col-sm-3 control-label">姓名</label>
              <div class="col-md-4 col-sm-6">
                <input type="text" class="form-control" name="name" value="${sessionScope._user.username}">
              </div>
            </div>
            <div class="form-group">
              <label for="phone" class="col-md-2 col-sm-3 control-label">手机号码</label>
              <div class="col-md-4 col-sm-6">
                <input type="text" class="form-control" name="phone" value="${sessionScope._user.phone}">
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <input type="hidden" name="courseId" value="${course.id}">
            <button type="submit" class="btn btn-success">提交报名</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
          </div>
        </form>
      </div>
      <div class="application-pay" style="display: none;">
        <div class="modal-body">
          <div class="row">
            <div class="col-md-2 col-sm-3">
              <div class="icon-lg icon-center icon-ok">
                <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
              </div>
            </div>
            <div class="col-md-10 col-sm-9">
              <div class="application-title t-content-medium">课程订单等待支付。</div>
              <div class="row-space-top-3 actions-pay">
                <a class="btn btn-success" href="/order/pay?type=60000&dataId=${course.id}" target="_blank">去支付</a><a class="btn btn-default col-space-4"
                  href="/order/pay/result?type=60000&dataId=${course.id}">支付完成，查看我的课程</a>
              </div>
              <div class="row-space-top-3">
                <a class="btn-link" href="/order/pay/problems" target="_blank">支付遇到问题？</a>
              </div>
            </div>
          </div>
          <div class="row row-space-top-3 row-space-2 tip">
            <hr />
            <div class="col-md-12">
              <label>提示：</label><span>对所有课程均支持，自报名起24小时内无条件取消报名并全额退款。请放心使用。</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="/js/course.js?v=20161220008"></script>