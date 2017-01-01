<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>

<div class="container">
  <div class="row row-space-top-2">
    <div class="col-md-9 col-sm-9 col-xs-12" id="courses">
      <div class="section">
        <div class="title lg">实践课列表</div>
        <c:forEach var="item" items="${courses}">
          <div class="panel panel-default section-course" data-id="${item.id}">
            <div class="panel-body">
              <div class="row">
                <div class="col-md-4 col-sm-5 col-sx-6">
                  <c:choose>
                    <c:when test="${not empty item.image}">
                      <img alt="翻译资源网-实践课图片" src="http://transkip.b0.upaiyun.com/${item.image}!M">
                    </c:when>
                    <c:otherwise>
                      <img alt="翻译资源网-实践课图片" src="http://transkip.b0.upaiyun.com/201612/course-cover.png!M">
                    </c:otherwise>
                  </c:choose>
                </div>
                <div class="col-md-8 col-sm-7 col-sx-6">
                  <div class="section-title lg">
                    <a href="/course/${item.id}" target="_blank">${item.name}</a>
                  </div>
                  <div class="section-caption row-space-top-1">
                    <a href="/instructor/${item.properties.instructor.id}" target="_blank">${item.properties.instructor.name}</a>
                  </div>
                  <div class="section-content fixed row-space-top-1">
                    <a href="/course/${item.id}" target="_blank">${item.content}</a>
                  </div>
                  <div class="section-footer row-space-top-1">
                    <span class="text-muted">${item.subscript}</span><span class="course-price pull-right"><c:choose>
                        <c:when test="${item.free eq 1}">免费</c:when>
                        <c:otherwise>¥ ${item.price}</c:otherwise>
                      </c:choose></span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>

        <ul class="pagination">
          <c:if test="${currentPage ne 1}">
            <li><a href="/course">&laquo;</a></li>
          </c:if>
          <c:forEach var="p" items="${pages}">
            <c:choose>
              <c:when test="${p ne currentPage}">
                <li><a href="/course?p=${p}">${p}</a></li>
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
      </div>
    </div>

    <div class="col-md-3 col-sm-3 col-xs-6">
      <div class="section row-space-top-3">
        <a class="btn orange" href="/courses" target="_blank">我的课程</a>
      </div>
    </div>

  </div>
</div>