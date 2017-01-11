<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>

<div class="panel panel-default row-space-4">
  <div class="panel-heading">基本资料修改</div>
  <div class="panel-body">
    <form id="profile-form" name="profile-form" action="/settings/profile" method="post" class="form-horizontal" role="form">
      <div class="form-group">
        <label for="username" class="col-md-2 col-sm-2 control-label">昵称</label>
        <div class="col-md-4 col-sm-4">
          <input type="text" name="username" class="form-control" value="${user.username}" />
        </div>
      </div>
      <div class="form-group">
        <label for="gender" class="col-md-2 col-sm-2 control-label">性别</label>
        <div class="col-md-4 col-sm-4">
          <div class="radio">
            <label><input type="radio" name="gender" value="F" <c:if test="${'F' eq user.gender}">checked</c:if>>女</label>
          </div>
          <div class="radio">
            <label><input type="radio" name="gender" value="M" <c:if test="${'M' eq user.gender}">checked</c:if>>男</label>
          </div>
        </div>
      </div>
      <div class="form-group">
        <label for="profile" class="col-md-2 col-sm-2 control-label">头像</label>
        <div class="col-md-3 col-sm-3 col-xs-6 image-container">
          <c:choose>
            <c:when test="${not empty user.avatar}">
              <img src="http://yikeyun.b0.upaiyun.com/${user.avatar}!M" alt="${user.username}">
            </c:when>
            <c:otherwise>
              <img src="http://yikeyun.b0.upaiyun.com/static/user-avatar.png!M" alt="${user.username}">
            </c:otherwise>
          </c:choose>
        </div>
        <div class="col-md-4 col-sm-4 col-xs-6">
          <a class="btn btn-default" data-toggle="modal" data-target="#imageModal">设置头像</a>
        </div>
      </div>
      <div class="form-group">
        <label for="profile" class="col-md-2 col-sm-2 control-label">个人简介</label>
        <div class="col-md-6 col-sm-6">
          <textarea name="profile" rows="3" class="form-control">${user.profile}</textarea>
        </div>
      </div>
      <div class="form-group">
        <div class="col-md-6 col-md-offset-2 col-sm-6 col-sm-offset-2">
          <input name="avatar" type="hidden">
          <button type="submit" class="btn btn-danger">保存</button>
        </div>
      </div>
    </form>
  </div>
</div>
<%-- <c:if test="${not empty instructor}">
  <div class="panel panel-default row-space-4">
    <div class="panel-heading">老师资料修改</div>
    <div class="panel-body">
      <form id="teacherInfo-form" name="profile-form" action="/api/v1/instructor/update" method="post" class="form-horizontal" role="form">
        <div class="form-group">
          <label for="name" class="col-md-2 col-sm-2 control-label">姓名</label>
          <div class="col-md-4 col-sm-4">
            <input type="text" name="name" class="form-control" value="${instructor.name}" />
          </div>
        </div>

        <div class="form-group">
          <label for="profile" class="col-md-2 col-sm-2 control-label">头像</label>
          <div class="col-md-3 col-sm-3 col-xs-6 image-container">
            <c:choose>
              <c:when test="${not empty instructor.avatar}">
                <img src="http://yikeyun.b0.upaiyun.com/${instructor.avatar}!M" alt="${instructor.avatar}">
              </c:when>
              <c:otherwise>
                <img src="http://yikeyun.b0.upaiyun.com/static/user-avatar.png!M" alt="${instructor.avatar}">
              </c:otherwise>
            </c:choose>
          </div>
          <div class="col-md-4 col-sm-4 col-xs-6">
            <a class="btn btn-default" data-toggle="modal" data-target="#imageModalTeacher">设置头像</a>
          </div>
        </div>
        <div class="form-group">
          <label for="contacts" class="col-md-2 col-sm-2 control-label">联系方式</label>
          <div class="col-md-4 col-sm-4">
            <input type="text" name="contacts" class="form-control" value="${instructor.contacts}" />
          </div>
        </div>
        <div class="form-group">
          <label for="profile" class="col-md-2 col-sm-2 control-label">简介</label>
          <div class="col-md-6 col-sm-6">
            <textarea name="profile" rows="3" class="form-control">${instructor.profile}</textarea>
          </div>
        </div>
        <div class="form-group">
          <div class="col-md-6 col-md-offset-2 col-sm-6 col-sm-offset-2">
            <input name="avatar" type="hidden">
            <button type="submit" class="btn btn-danger">保存</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</c:if> --%>
<div class="modal fade" id="imageModal" tabindex="-1" role="dialog" aria-labelledby="imageModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title" id="imageModalLabel">设置头像</h4>
      </div>
      <div class="modal-body">
        <div class="form-group clearfix">
          <div class="col-md-12 col-sm-12 col-xs-12">
            <span class="btn btn-default file-input-container"> 从电脑选择并上传图片 <input type="file" name="imageFile" class="fileupload"></span>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default op-cancel" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>
<div class="modal fade" id="imageModalTeacher" tabindex="-1" role="dialog" aria-labelledby="imageModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title" id="imageModalLabel">设置头像</h4>
      </div>
      <div class="modal-body">
        <div class="form-group clearfix">
          <div class="col-md-12 col-sm-12 col-xs-12">
            <span class="btn btn-default file-input-container"> 从电脑选择并上传图片 <input type="file" name="imageFile" class="fileupload"></span>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default op-cancel" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>
<script src="/js/user.js?v=20161015001"></script>
<script src="/libs/jquery/jquery.ui.widget.js"></script>
<script src="/libs/jquery/jquery.iframe-transport.js"></script>
<script src="/libs/jquery/jquery.fileupload.js"></script>
<script>
	$('.fileupload')
			.fileupload(
					{
						url : '/api/v1/image',
						dataType : 'json',
						add : function(e, data) {
							data.submit();
						},
						done : function(e, data) {
							var resp = data.result;
							if (resp && resp.e == 0) {
								var image = resp.r;
								$('input[name=avatar]', $('#profile-form'))
										.val(image.path);
								$('.image-container', $('#profile-form'))
										.html(
												'<img alt="头像照片" src="' + image.url + '">');
								$('#imageModal').modal('hide');
							}
						},
						progressall : function(e, data) {
							// update progress.
							var progress = parseInt(data.loaded / data.total
									* 100, 10);
							$('#progress .progress-bar').css('width',
									progress + '%');
						}
					}).prop('disabled', !$.support.fileInput).parent()
			.addClass($.support.fileInput ? undefined : 'disabled');
</script>