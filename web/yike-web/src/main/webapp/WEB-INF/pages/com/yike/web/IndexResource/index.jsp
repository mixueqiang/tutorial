<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="marketing-banner hero-top">
  <div class="hero-header">
    <div class="container">
      <a class="hero-brand xxl" href="/">一课</a>
      <ul class="nav pull-right">
        <li class="pull-left"><a class="btn btn-success" href="/course/create">发布一门实践课程</a></li>
        <c:choose>
          <c:when test="${not empty _user}">
            <li class="pull-left col-space-2"><a href="/dashboard">${_user.phone}</a></li>
            <li class="pull-left col-space-2"><a href="/signout">退出</a></li>
          </c:when>
          <c:otherwise>
            <li class="pull-left col-space-2"><a href="/signin">登录</a></li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </div>
  <div class="hero-overlay"></div>
  <div class="intro-area text-center">
    <div class="col-middle content">
      <h1>学习，并且实战</h1>
      <h2 class="subtitle"></h2>
      <a class="btn btn-success row-space-top-6" href="/signup" target="_blank">注册</a>
    </div>
  </div>
</div>

<div class="marketing-courses row-space-top-10">
  <div class="container">
    <div class="row">
      <div class="col-md-12">
        <c:forEach var="item" items="${categories}">
          <div class="row">
            <div class="col-md-12">
              <div class="title">
                <div class="lg pull-left">${item.name}</div>
                <div class="md pull-right">
                  <a href="/course?c=${item.id}">更多课程</a>
                </div>
              </div>
            </div>
          </div>
          <div class="row row-space-top-1">
            <c:forEach var="item" items="${courses[item.id]}">
              <div class="col-md-3 col-sm-6">
                <div class="section">
                  <div class="section-cover">
                    <c:choose>
                      <c:when test="${not empty item.image}">
                        <img alt="一课-课程图片" src="http://yikeyun.b0.upaiyun.com/${item.image}!M">
                      </c:when>
                      <c:otherwise>
                        <img alt="一课-课程图片" src="http://yikeyun.b0.upaiyun.com/static/course-cover.png!M">
                      </c:otherwise>
                    </c:choose>
                  </div>
                  <div class="section-title md">
                    <a href="/course/${item.id}" target="_blank">${item.name}</a>
                  </div>
                  <div class="section-caption row-space sm">${item.properties.instructor.name}</div>
                  <div class="section-content row-space ln-1">${item.content}</div>
                  <div class="section-footer">
                    <span class="text-muted">${item.superscript}</span><span class="course-price pull-right">¥${item.price}</span>
                  </div>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>

<div class="marketing-banner hero-bottom row-space-top-10">
  <div class="intro-area text-center">
    <div class="col-middle content">
      <h1>每位有工作经验的人都是老师</h1>
      <h2 class="subtitle"></h2>
      <a class="btn btn-success row-space-top-6" href="/">发布一门实践课程</a>
    </div>
  </div>
</div>