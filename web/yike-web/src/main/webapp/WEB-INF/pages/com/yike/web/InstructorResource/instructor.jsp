<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container">
  <div class="row row-space-top-4 section">
    <div class="col-md-3 col-sm-3 col-xs-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <div class="row">
            <div class="col-sm-12 col-xs-4">
              <c:choose>
                <c:when test="${not empty instructor.avatar}">
                  <img alt="一课-用户头像" src="http://yikeyun.b0.upaiyun.com/${instructor.avatar}!M">
                </c:when>
                <c:otherwise>
                  <img alt="一课-用户头像" src="http://yikeyun.b0.upaiyun.com/static/user-avatar.png!M">
                </c:otherwise>
              </c:choose>
            </div>
            <div class="col-sm-12 col-xs-5">
              <h3 class="text-center">${instructor.name}</h3>
            </div>
            <div class="col-sm-12 col-xs-5">
              <p>${instructor.profile}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="col-md-9 col-sm-9 col-xs-12" id="courses">
      <div id="hint" class="alert alert-danger" role="alert" style="dispaly: none;">没有发布的课程！</div>
      <div id="contentList">
        <c:forEach var="item" items="${courses}">
          <div class="panel panel-default section-course" data-id="${item.id}">
            <div class="panel-body">
              <div class="row  row-top">
                <div class="col-sx-8 col-sm-4 col-md-4">
                  <c:choose>
                    <c:when test="${not empty item.image}">
                      <img alt="一课-课程" src="http://yikeyun.b0.upaiyun.com/${item.image}!M">
                    </c:when>
                    <c:otherwise>
                      <img alt="一课-课程" src="http://yikeyun.b0.upaiyun.com/static/course-cover.png!M">
                    </c:otherwise>
                  </c:choose>
                </div>
                <div class="col-sx-8 col-sm-8 col-md-8">
                  <div class="row">
                    <div class="col-md-8 col-sm-8 col-xs-12">
                      <a class="courses-title" href="/course/${item.id}" target="_blank">${item.name}</a>
                    </div>
                    <div class="col-md-4 col-sm-5 col-xs-12">
                      <span class="courses-superscript">${item.superscript}</span>
                    </div>
                  </div>
                  <a class="courses-name" href="/instructor/${item.properties.instructor.id}" target="_blank">${item.properties.instructor.name}</a> <a id="contentStr" class="courses-content"
                    href="/course/${item.id}" target="_blank">${item.content}</a>
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
  </div>
</div>

<script src="/js/course.js?v=20161220004"></script>
<script>
  $(function() {
    if ($('#contentList').text().trim()) {
      $('#hint').hide();
    } else {
      $('#hint').show();
    }
  })
</script>
