<%--
  Created by IntelliJ IDEA.
  User: ilakeyc
  Date: 2017/3/3
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/taglibs.jsp" %>
<ol class="breadcrumb">
  <li><a href="/admin">Home</a></li>
  <li class="active">课程表</li>
  <c:if test="${oneCourse eq true}">
    <li><a style="color: green;" href="/admin/schedule">全部</a></li>
  </c:if>
</ol>

<div class="container">
  <div class="row">
    <c:if test="${oneCourse eq true}">
      <div class="container">
        <div class="row">
          <div class="col-md-12">
            <strong>当前课程：${course.name}</strong>
            <a href="javasctipt:;" class="btn btn-danger pull-right" role="button" data-toggle="modal" data-target="#newModal">生成新的课程表</a>
          </div>
        </div>
      </div>
    </c:if>
    <div class="col-md-12">
      <div class="panel">
        <div class="panel-body panel-default">
          <table class="table table-condensed table-hover" id="orders">
            <thead>
            <tr>
              <th>id</th>
              <c:if test="${oneCourse ne true}">
                <th style="width: 30%;">课程</th>
              </c:if>
              <th>课程开始时间</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${schedules}">
              <tr data-id="${item.id}">
                <td>${item.id}</td>
                <c:if test="${oneCourse ne true}">
                  <td><a href="/admin/schedule?courseId=${item.properties.course.id}">${item.properties.course.name}</a>
                  </td>
                </c:if>
                <td>${item.launchDate} ${item.launchTime}</td>
                <td><a href="javascript:;" data-toggle="modal" data-target="#editModal">编辑</a></td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
        <c:if test="${oneCourse eq true}">
          <div style="text-align:center"><a href="javasctipt:;" class="btn btn-danger" role="button" data-toggle="modal" data-target="#addModal">添加新日程</a></div>
        </c:if>
      </div>
      

      <ul class="pagination">
        <c:if test="${currentPage ne 1}">
          <li><a href="${uriPrefix}1">&laquo;</a></li>
        </c:if>
        <c:forEach var="p" items="${pages}">
          <c:choose>
            <c:when test="${p ne currentPage}">
              <li><a href="${uriPrefix}${p}">${p}</a></li>
            </c:when>
            <c:otherwise>
              <li class="active"><a href="#">${p}</a></li>
            </c:otherwise>
          </c:choose>
        </c:forEach>
        <c:if test="${lastPage > 0 and currentPage ne lastPage}">
          <li><a href="${uriPrefix}${lastPage}">&raquo;</a></li>
        </c:if>
      </ul>
    </div>
  </div>
</div>


<!-- newModal -->
<div class="modal fade" id="newModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">生成课程表</h4>
      </div>
      <div class="modal-body">
        <h4>${course.name}</h4>
        <form id="new" class="form-horizontal row-space-top-2" action="/admin/schedule" method="post">
        <div class="form-group hide">
            <label for="courseId" class="col-sm-3 control-label">课程ID：</label>
            <div class="col-sm-9">
              <input type="text" class="form-control" id="courseId" name="courseId" value="${course.id}" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="date" class="col-sm-3 control-label">开始日期：</label>
            <div class="col-sm-9">
              <input type="date" class="form-control" id="date" name="date" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="time" class="col-sm-3 control-label">开始时间：</label>
            <div class="col-sm-9">
              <input type="time" class="form-control" id="time" name="time" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="daysOfWeek" class="col-sm-3 control-label">每周几开课：</label>
            <div class="col-sm-9">
              <label class="checkbox-inline">
                <input type="checkbox" id="inlineCheckbox1" name="daysOfWeek" value="2">周一
              </label>
              <label class="checkbox-inline">
                <input type="checkbox" id="inlineCheckbox2" name="daysOfWeek" value="3">周二
              </label>
              <label class="checkbox-inline">
                <input type="checkbox" id="inlineCheckbox3" name="daysOfWeek" value="4">周三
              </label>
              <label class="checkbox-inline">
                <input type="checkbox" id="inlineCheckbox1" name="daysOfWeek" value="5">周四
              </label>
              <label class="checkbox-inline">
                <input type="checkbox" id="inlineCheckbox2" name="daysOfWeek" value="6">周五
              </label>
              <label class="checkbox-inline">
                <input type="checkbox" id="inlineCheckbox3" name="daysOfWeek" value="7">周六
              </label>
              <label class="checkbox-inline">
                <input type="checkbox" id="inlineCheckbox3" name="daysOfWeek" value="1">周日
              </label>
            </div>
          </div>
          <div class="form-group">
            <label for="totalCount" class="col-sm-3 control-label">总课时：</label>
            <div class="col-sm-9">
              <input type="number" class="form-control" id="totalCount" name="totalCount" placeholder="">
            </div>
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-primary" id="submitt">提交</button>
          </div>
        </form>
      </div>
      
    </div>
  </div>
</div>

<!-- editModal -->
<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">编辑课程</h4>
      </div>
      <div class="modal-body">
        <h4>${course.name}</h4>
        <form id="new" class="form-horizontal row-space-top-2" action="/admin/schedule" method="post">
          <div class="form-group">
            <label for="date" class="col-sm-3 control-label">开始日期：</label>
            <div class="col-sm-9">
              <input type="date" class="form-control" id="date" name="date" value="" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="time" class="col-sm-3 control-label">开始日期：</label>
            <div class="col-sm-9">
              <input type="time" class="form-control" id="time" name="time" placeholder="">
            </div>
          </div>
          
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default">删除</button>
        <button type="button" class="btn btn-primary">提交</button>
      </div>
    </div>
  </div>
</div>

<!-- addModal -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">添加课程</h4>
      </div>
      <div class="modal-body">
        <h4>${course.name}</h4>
        <form id="new" class="form-horizontal row-space-top-2" action="/admin/schedule" method="post">
          <div class="form-group">
            <label for="date" class="col-sm-3 control-label">开始日期：</label>
            <div class="col-sm-9">
              <input type="date" class="form-control" id="date" name="date" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="time" class="col-sm-3 control-label">开始日期：</label>
            <div class="col-sm-9">
              <input type="time" class="form-control" id="time" name="time" placeholder="">
            </div>
          </div>
          
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>
<script>
/*$('#submitt').click(function(){

})
$('#new').validate({
  rules : {
    date : {
      required : true
    },
    time : {
      required : true
    },
    daysOfWeek : {
      required : true
    },
    totalCount : {
      required : true,
      maxlength : 100,
      digits : true
    }
  },
  messages : {
    date : {
      required : '请输入开始日期。',
      maxlength : $.format("课程名称不能超过 {0} 个字。")
    },
    time : {
      required : '请输入开始时间。',
      maxlength : $.format("课程亮点不能超过 {0} 个字。")
    },
    daysOfWeek : {
      required : '请输入每周几开课。',
      maxlength : $.format("课程价格最多可以输入 {0} 个字符。"),
      number : "请输入有效的数字。",
    },
    totalCount : {
      required : '请输入总课时。',
      maxlength : $.format("招生人数不能超过 {0} 个字。"),
      digits : "只能输入数字",
    }
  },
  submitHandler : function(form) {
    var $btn = $('button[type=submit]', $(form));
    $btn.attr('disabled', 'disabled').addClass('disabled');
    if (!$(form).valid()) {
      $('.error').eq(0).focus();
      $btn.removeAttr('disabled').removeClass('disabled');
      return false;
    }
    $(form).ajaxSubmit({
      success : function(resp) {
        if (resp && resp.e == 0) {
          $('input', $(form)).val('');
          $btn.removeAttr('disabled').removeClass('disabled');
          var id = resp.r;
          Message.info('生成课程表成功。', false, $('.form-group:last', $(form)));

        } else {
          $btn.removeAttr('disabled').removeClass('disabled');
          Message.error('生成课程表失败：' + resp.m, false, $('.form-group:last', $(form)));
        }
      },
      error : function() {
        $btn.removeAttr('disabled').removeClass('disabled');
        Message.error('生成课程表失败！', false, $('.form-group:last', $(form)));
      }
    });
  }
});*/

</script>