<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container row-space-top-4">
   <div class="row row-space-top-4">
      <div class="col-xs-8 col-xs-offset-2 col-md-8 col-md-offset-2">
        <h4>课程名称：${course.name}</h4>
         <table class="table table-striped table-hover">
            <tr>
              <th>昵称</th>
              <th>报名日期</th>
              <th>姓名</th>
              <th>联系方式</th>
            </tr>
            <c:forEach var="item" items="${students}">
             <tr>
                <td>${item.nickname}</td>
                <td><jsp:useBean id="postDate" class="java.util.Date" /> <jsp:setProperty name="postDate" property="time" value="${item.createTime}" /> <fmt:formatDate value="${postDate}"
                      pattern="yyyy-MM-dd HH:mm" timeZone="GMT+0800" /></td>
                <td>${item.name}</td>
                <td>${item.phone}</td>
              </tr>
            </c:forEach>
         </table>
      </div>
   </div>
</div>