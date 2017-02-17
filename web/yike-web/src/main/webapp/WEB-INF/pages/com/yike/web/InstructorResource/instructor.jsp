<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container">
  <div class="row row-space-top-4 section">
    <div class="col-md-3 col-sm-3 col-xs-4">
      <div class="panel panel-default">
        <div class="panel-body">
          <div class="row">
            <div class="col-sm-12 col-xs-12">
              <c:choose>
                <c:when test="${not empty instructor.avatar}">
                  <img alt="一课上手-用户头像" src="http://yikeyun.b0.upaiyun.com/${instructor.avatar}!M">
                </c:when>
                <c:otherwise>
                  <img alt="一课上手-用户头像" src="http://yikeyun.b0.upaiyun.com/static/user-avatar.png!M">
                </c:otherwise>
              </c:choose>
            </div>
            <div class="col-sm-12 col-xs-12">
              <h3 class="text-center">${instructor.name}</h3>
            </div>
            <div class="col-sm-12 col-xs-12">
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
                    <div class="courses-title section-title col-md-8 col-sm-7 col-xs-12">
                      <a href="/course/${item.id}" target="_blank">${item.name}</a>
                    </div>
                    <div class="col-md-4 col-sm-5 col-xs-12">
                      <span class="courses-superscript">${item.superscript}</span>
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
  function Heightadapt(){
    /*列表图片成比例*/
    $('.courses-imgcover .courses-img').height($('.courses-imgcover .courses-img').width()*0.60);
    /*下角标位置动态问题*/
    $('.courses-footer').css({
      left:$('.courses-details').position().left,
      width:$('.courses-details').width()
    })
  }
  window.onload=function(){
    Heightadapt();
  }
  window.onresize=function(){
    Heightadapt();
  }
</script>
