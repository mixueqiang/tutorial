<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="panel panel-default row-space-4">
  <div class="panel-heading">分享我的动态</div>
  <div class="panel-body">
    <a class="btn btn-warning" href="/feed/create">分享我的动态</a>
  </div>
</div>

<c:if test="${sessionScope._user.worker}">
  <div class="panel panel-default row-space-4">
    <div class="panel-body">
      <a href="/termbases">管理我的术语库</a>
    </div>
  </div>

  <c:if test="${workerInvitationCount gt 0}">
    <div class="panel panel-default row-space-4">
      <div class="panel-heading">工作邀请消息（${workerInvitationCount}条新消息）</div>
      <table class="table table-hover">
        <thead>
          <tr>
            <th style="width: 70%;">职位名称</th>
            <th>邀请时间</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="workerInvitation" items="${workerInvitations}">
            <tr>
              <td><a href="/worker/invitation/${workerInvitation.id}/read" title="查看职位">${workerInvitation.job.name}</a></td>
              <td><jsp:useBean id="inviteDate" class="java.util.Date" /><jsp:setProperty name="inviteDate" property="time" value="${workerInvitation.createTime}" /><fmt:formatDate
                  value="${inviteDate}" pattern="yyyy-MM-dd HH:mm" timeZone="GMT+0800" /></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </c:if>
</c:if>
<c:if test="${jobApplicationCount gt 0}">
  <div class="panel panel-default row-space-4">
    <div class="panel-heading">招聘简历消息（${jobApplicationCount}条新消息）</div>
    <table class="table table-hover">
      <thead>
        <tr>
          <th style="width: 70%;">职位名称</th>
          <th>简历数量</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="job" items="${jobApplications}">
          <tr>
            <td>${job.name}</td>
            <td>${job.applicationCount}</td>
            <td><a href="/job/application?jobId=${job.id}" title="查看简历"><span class="glyphicon glyphicon-th-list" aria-hidden="true"></span></a></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</c:if>