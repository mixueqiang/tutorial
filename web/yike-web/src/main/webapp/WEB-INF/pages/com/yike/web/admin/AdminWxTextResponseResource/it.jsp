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
                <td><a id="editButton" class="glyphicon glyphicon-pencil" href="javascript:;" title="编辑" role="button"
                       data-toggle="modal" data-target="#editModal"></a></td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
        <div style="text-align:center"><a href="javascript:;" class="btn btn-danger" role="button" data-toggle="modal"
                                          data-target="#addModal">添加</a></div>
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

<!-- addModal -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
            aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="addEntityLabel">添加内容</h4>
      </div>
      <div class="modal-body">
        <form id="add" class="form-horizontal row-space-top-2" action="/admin/schedule/add" method="post">
          <div class="form-group">
            <label for="addtarget" class="col-sm-3 control-label">根据内容：</label>
            <div class="col-sm-9">
              <input type="target" class="form-control" id="addTarget" name="target" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="addresult" class="col-sm-3 control-label">回复内容：</label>
            <div class="col-sm-9">
              <textarea class="form-control" id="addResult" name="result" placeholder=""></textarea>
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

<!-- editModal -->
<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
            aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="editEntityLabel">编辑内容</h4>
      </div>
      <div class="modal-body">
        <form id="edit" class="form-horizontal row-space-top-2" action="/admin/wx/edit" method="post">
          <div class="form-group hide">
            <label for="responseId" class="col-sm-3 control-label">ID：</label>
            <div class="col-sm-9">
              <input type="text" class="form-control" id="responseId" name="responseId" value="" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="editTarget" class="col-sm-3 control-label">根据内容：</label>
            <div class="col-sm-9">
              <input type="target" class="form-control" id="editTarget" name="target" value="" placeholder="">
            </div>
          </div>
          <div class="form-group">
            <label for="editresult" class="col-sm-3 control-label">回复内容：</label>
            <div class="col-sm-9">
              <textarea class="form-control" id="editResult" name="result" value="" placeholder=""></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" id="delete" class="btn btn-default">删除</button>
            <button type="submit" class="btn btn-primary">提交</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
