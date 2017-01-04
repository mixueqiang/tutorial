<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="panel panel-default">
  <div class="panel-heading">消息通知设置</div>
  <div class="panel-body">
    <form id="settings-form" name="settings-form" action="/settings/notification" method="post" role="form">
      <div class="form-group">
        <label for="emailSettingJob">是否接收翻译资源网的工作邮件通知？</label>
        <div class="radio">
          <label><input type="radio" name="emailSettingJob" value="1" <c:if test="${1 eq worker.emailSettingJob}">checked</c:if>>接收</label>
        </div>
        <div class="radio">
          <label><input type="radio" name="emailSettingJob" value="0" <c:if test="${0 eq worker.emailSettingJob}">checked</c:if>>不接收</label>
        </div>
      </div>
      <div class="form-group">
        <label for="emailSettingInvitation">是否接收翻译公司或客户发出的工作或项目邀请？</label>
        <div class="radio">
          <label><input type="radio" name="emailSettingInvitation" value="1" <c:if test="${1 eq worker.emailSettingInvitation}">checked</c:if>>接收</label>
        </div>
        <div class="radio">
          <label><input type="radio" name="emailSettingInvitation" value="0" <c:if test="${0 eq worker.emailSettingInvitation}">checked</c:if>>不接收</label>
        </div>
      </div>
      <div class="form-group">
        <button type="submit" class="btn btn-danger">提交</button>
      </div>
    </form>
  </div>
</div>

<script src="/js/settings.js?v=20160914001"></script>