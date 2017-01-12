<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<style>
.course-nav-row {
  padding: 16px 0 5px; border-bottom: 1px solid #edf1f2;
}

.course-nav-row .hd {
  width: 80px; height: 30px; line-height: 30px; font-weight: 700; font-size: 14px; color: #07111b; text-align: right;
}

.course-nav-row .bd {
  margin-left: 64px;
}

.course-nav-item {
  display: inline-block; margin: 0 6px;
}

.course-nav-item.on a {
  background: #337ab7; color: #fff;
}

.course-nav-item a {
  display: block; line-height: 14px; margin-bottom: 10px; padding: 9px 12px; font-size: 14px; border-radius: 2px;
}

.course-nav-item a:active, .course-nav-item a:hover {
  background: #236aa7; color: #fff;
}
.course-content{
  position: relative;
}
.courses-imgcover{

}
.courses-img{
  
}
.courses-details{
   
}
.courses-title{
  margin-top:10px;
}
.courses-footer{
  font-size: 14px;
  padding-left: 15px;
  position: absolute;
  bottom: 0;
  left:0px;
}
</style>
<div class="container">
  <div class="course-nav-row clearfix">
    <span class="hd pull-left">课程分类：</span>
    <div class="bd">
      <c:choose>
        <c:when test="${empty currentCategoryId}">
          <p class="course-nav-item on">
            <a href="/course">全部</a>
          </p>
        </c:when>
        <c:otherwise>
          <p class="course-nav-item">
            <a href="/course">全部</a>
          </p>
        </c:otherwise>
      </c:choose>

      <c:forEach var="item" items="${categories}">
        <c:choose>
          <c:when test="${currentCategoryId eq item.id}">
            <p class="course-nav-item on">
              <a href="/course?c=${item.id}">${item.name}</a>
            </p>
          </c:when>
          <c:otherwise>
            <p class="course-nav-item">
              <a href="/course?c=${item.id}">${item.name}</a>
            </p>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </div>
  </div>
  <div class="row row-space-top-2">
    <div class="col-md-12 col-sm-12 col-xs-12" id="courses">

      <div class="section">
        <div class="title lg">实践课列表</div>
        <c:forEach var="item" items="${courses}">
          <div class="panel panel-default section-course" data-id="${item.id}">
            <div class="panel-body">
              <div class="course-content row">
                <div class="courses-imgcover col-md-4 col-sm-5 col-sx-6">
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
                  <div class="courses-title section-title lg">
                    <a href="/course/${item.id}" target="_blank">${item.name}</a>
                  </div>
                  <div class="section-caption row-space-top-1">
                    <a href="/instructor/${item.properties.instructor.id}" target="_blank">${item.properties.instructor.name}</a>
                  </div>
                  <div class="section-content fixed row-space-top-1">
                    <a href="/course/${item.id}" target="_blank">${item.content}</a>
                  </div>
                </div>
                <div class="courses-footer row-space-top-1">
                  <span class="text-muted">${item.subscript}</span><span class="courses-price pull-right"><c:choose>
                      <c:when test="${item.free eq 1}">免费</c:when>
                      <c:otherwise>¥ ${item.price}</c:otherwise>
                    </c:choose></span>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>

        <c:choose>
          <c:when test="${empty currentCategoryId}">
            <ul class="pagination">
              <c:if test="${currentPage ne 1}">
                <li><a href="/course">&laquo;</a></li>
              </c:if>
              <c:forEach var="p" items="${pages}">
                <c:choose>
                  <c:when test="${p ne currentPage}">
                    <li><a id="page" href="/course?p=${p}">${p}</a></li>
                  </c:when>
                  <c:otherwise>
                    <li class="active"><a href="#">${p}</a></li>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              <c:if test="${lastPage > 0 and currentPage ne lastPage}">
                <li><a href="/course?p=${lastPage}">&raquo;</a></li>
              </c:if>
            </ul>
          </c:when>
          <c:otherwise>
            <ul class="pagination">
              <c:if test="${currentPage ne 1}">
                <li><a href="/course?c=${currentCategoryId}">&laquo;</a></li>
              </c:if>
              <c:forEach var="p" items="${pages}">
                <c:choose>
                  <c:when test="${p ne currentPage}">
                    <li><a id="page" href="/course?c=${currentCategoryId}&p=${p}">${p}</a></li>
                  </c:when>
                  <c:otherwise>
                    <li class="active"><a href="#">${p}</a></li>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              <c:if test="${lastPage > 0 and currentPage ne lastPage}">
                <li><a href="/course?c=${currentCategoryId}&p=${lastPage}">&raquo;</a></li>
              </c:if>
            </ul>
          </c:otherwise>
        </c:choose>

      </div>
    </div>
  </div>
</div>
<script>
function Heightadapt(){
  /*列表图片成比例*/
  $('.courses-imgcover .courses-img').height($('.courses-imgcover .courses-img').width()*0.60);
  /*下角标位置动态问题*/
  $('.courses-footer').css({
    left:$('.courses-details').position().left,
    width:$('.courses-details').width()
  })
}
Heightadapt();
window.onresize=function(){
  Heightadapt();
}
</script>