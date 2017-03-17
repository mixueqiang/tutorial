<%--
  Created by IntelliJ IDEA.
  User: ilakeyc
  Date: 2017/3/17
  Time: 14:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/taglibs.jsp" %>
<ol class="breadcrumb">
  <li><a href="/admin">Home</a></li>
  <li class="active">IT技术成长联盟公众号-自动回复内容</li>
</ol>

<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <table class="table table-condensed table-hover" id="responses">
            <thead>
            <tr>
              <th style="width: 3%;">ID</th>
              <th>根据</th>
              <th>返回内容</th>
              <th>添加时间</th>
              <th>修改时间</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${responses}">
              <tr data-id="${item.id}" data-target="${item.target}" data-result="${item.result}">
                <td>${item.id}</td>
                <td>${item.target}</td>
                <td>${item.result}</td>
                <td>
                  <jsp:useBean id="createDate" class="java.util.Date"/>
                  <jsp:setProperty name="createDate" property="time" value="${item.createTime}"/>
                  <fmt:formatDate
                      value="${createDate}" pattern="MM-dd HH:mm" timeZone="GMT+0800"/></td>
                <td>
                  <jsp:useBean id="updateDate" class="java.util.Date"/>
                  <jsp:setProperty name="updateDate" property="time" value="${item.updateTime}"/>
                  <fmt:formatDate
                      value="${updateDate}" pattern="MM-dd HH:mm" timeZone="GMT+0800"/></td>
                <td data-id="${item.id}" data-target="${item.target}" data-result="${item.result}"><a id="edit-button"
                                                                                                      class="glyphicon glyphicon-pencil edit-button"
                                                                                                      href="javascript:;"
                                                                                                      title="编辑"
                                                                                                      role="button"
                                                                                                      data-toggle="modal"
                                                                                                      data-target="#edit-modal"></a>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
        <div style="text-align:center"><a href="javascript:;" class="btn btn-danger" role="button" data-toggle="modal"
                                          data-target="#add-modal">添加</a></div>
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

<!-- editModal -->
<div class="modal fade" id="edit-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
            aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="editEntityLabel">编辑内容</h4>
      </div>
      <div class="modal-body">
        <form id="edit" class="form-horizontal row-space-top-2" action="/admin/wxtextresponse/it/edit" method="post">
          <div class="form-group hide">
            <label for="edit-id" class="col-sm-3 control-label">ID：</label>
            <div class="col-sm-9">
              <input type="number" class="form-control" id="edit-id" name="id" value="" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="edit-target" class="col-sm-3 control-label">根据内容：</label>
            <div class="col-sm-9">
              <input type="text" class="form-control" id="edit-target" name="target" value="" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="edit-result" class="col-sm-3 control-label">回复内容：</label>
            <div class="col-sm-9">
              <textarea class="form-control" id="edit-result" name="result" value="" placeholder=""></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default delete-button">删除</button>
            <button type="submit" class="btn btn-primary">提交</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- addModal -->
<div class="modal fade" id="add-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
            aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="add-entity-label">添加内容</h4>
      </div>
      <div class="modal-body">
        <form id="add" class="form-horizontal row-space-top-2" action="/admin/wxtextresponse/it" method="post">
          <div class="form-group">
            <label for="add-target" class="col-sm-3 control-label">根据内容：</label>
            <div class="col-sm-9">
              <input type="text" class="form-control" id="add-target" name="target" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="add-result" class="col-sm-3 control-label">回复内容：</label>
            <div class="col-sm-9">
              <textarea class="form-control" id="add-result" name="result" placeholder=""></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-primary">提交</button>
          </div>
        </form>
      </div>

    </div>
  </div>
</div>

<!-- deleteSuccsee -->
<div class="modal fade bs-example-modal-sm" id="delete-modal" tabindex="-1" role="dialog"
     aria-labelledby="mySmallModalLabel">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
            aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">
        确定要删除吗
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary delete-success-button" id="delete-success-button">确定</button>
      </div>
    </div>
  </div>
</div>
<script>
    $(function () {
        $('#edit').validate({
            rules: {
                target: {
                    required: true
                },
                result: {
                    required: true
                }
            },
            messages: {
                target: {
                    required: '请输入内容。',
                },
                result: {
                    required: '请输入回复内容。',
                }
            },
            submitHandler: function (form) {
                var $btn = $('button[type=submit]', $(form));
                $btn.attr('disabled', 'disabled').addClass('disabled');
                if (!$(form).valid()) {
                    $('.error').eq(0).focus();
                    $btn.removeAttr('disabled').removeClass('disabled');
                    return false;
                }
                $(form).ajaxSubmit({
                    success: function (resp) {
                        if (resp && resp.e == 0) {
                            $('input', $(form)).val('');
                            $('textarea', $(form)).val('');
                            $btn.removeAttr('disabled').removeClass('disabled');
                            Message.info('编辑内容成功。', false, $('.form-group:last', $(form)));
                            setTimeout(function () {
                                $('#edit-modal').modal('hide');
                                window.location.reload();
                            }, 1000)
                        } else {
                            $btn.removeAttr('disabled').removeClass('disabled');
                            Message.error('编辑内容失败：' + resp.m, false, $('.form-group:last', $(form)));
                        }
                    },
                    error: function () {
                        $btn.removeAttr('disabled').removeClass('disabled');
                        Message.error('编辑内容失败！', false, $('.form-group:last', $(form)));
                    }
                });
            }
        });

        //编辑时，给模态框赋值
        $('.edit-button').click(function () {
            var target = $(this).parent().attr('data-target');
            var result = $(this).parent().attr('data-result');
            var id = $(this).parent().attr('data-id');
            $('#edit-target').val(target);
            $('#edit-result').val(result);
            $('#edit-id').val(id);

            $('.delete-button').click(function () {
                $('#delete-modal').modal('show');
                $('.delete-success-button').click(function () {
                    $.post('/admin/wxtextresponse/it/delete', {'id': id}, function (resp) {
                        if (resp && resp.e == 0) {
                            $('#delete-modal').modal('hide');
                            $('#edit-modal').modal('hide');
                            window.location.reload();
                        } else {
                            alert("删除失败" + resp.m);
                            $('#delete-modal').modal('hide');
                        }
                    }, 'json');
                })
            })

        });

        $('#add').validate({
            rules: {
                target: {
                    required: true
                },
                result: {
                    required: true
                }
            },
            messages: {
                target: {
                    required: '请输入内容。',
                },
                result: {
                    required: '请输入回复内容。',
                }
            },
            submitHandler: function (form) {
                var $btn = $('button[type=submit]', $(form));
                $btn.attr('disabled', 'disabled').addClass('disabled');
                if (!$(form).valid()) {
                    $('.error').eq(0).focus();
                    $btn.removeAttr('disabled').removeClass('disabled');
                    return false;
                }
                $(form).ajaxSubmit({
                    success: function (resp) {
                        if (resp && resp.e == 0) {
                            $('input', $(form)).val('');
                            $btn.removeAttr('disabled').removeClass('disabled');
                            Message.info('添加内容成功。', false, $('.form-group:last', $(form)));
                            setTimeout(function () {
                                $('#add-modal').modal('hide');
                                window.location.reload();
                            }, 1000)
                        } else {
                            $btn.removeAttr('disabled').removeClass('disabled');
                            Message.error('添加内容失败：' + resp.m, false, $('.form-group:last', $(form)));
                        }
                    },
                    error: function () {
                        $btn.removeAttr('disabled').removeClass('disabled');
                        Message.error('添加内容失败！', false, $('.form-group:last', $(form)));
                    }
                });
            }
        });
    })


</script>