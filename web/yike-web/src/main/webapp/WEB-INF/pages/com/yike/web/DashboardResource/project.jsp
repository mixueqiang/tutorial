<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<link rel="stylesheet" href="/libs/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" />
<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="page-header">
        <h4 id="project-title">Manage Project - ${project.name}</h4>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-10 col-md-offset-1">
      <span class="pull-right"><a href="#newMilestoneModal" data-toggle="modal">New Milestone</a></span>
    </div>
  </div>
  <div class="row">
    <div class="col-md-10 col-md-offset-1">
      <table class="table data-table" id="projects">
        <thead>
          <tr>
            <th>Milestone Id</th>
            <th>Timeline</th>
            <th>Payment (USD)</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="item" varStatus="status" items="${milestones}">
            <tr data-id="${item.id}" <c:choose><c:when test="${status.count %2 == 1}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
              <td style="max-width: 20px;">${status.count}</td>
              <td>${item.startDate}-${item.endDate}</td>
              <td>$${item.price}</td>
              <td>${item.status}</td>
              <td><a href="#">Deposit</a></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>
<!-- newMilestoneModal -->
<div class="modal fade" id="newMilestoneModal" tabindex="-1" role="dialog" aria-labelledby="newMilestoneModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="newMilestoneModalLabel">New Milestone</h4>
      </div>
      <form id="milestone-form" action="/milestone/create" method="post" class="form-horizontal" role="form">
        <div class="modal-body">
          <div class="form-group">
            <label for="startDate" class="col-sm-3 control-label">Start Date</label>
            <div class="col-sm-9">
              <div class="input-group date" id="startDatePicker">
                <input type="text" class="form-control" id="startDate"> <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label for="endDate" class="col-sm-3 control-label">End Date</label>
            <div class="col-sm-9">
              <div class="input-group date" id="endDatePicker">
                <input type="text" class="form-control" id="endDate"> <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label for="price" class="col-sm-3 control-label">Milestone Price</label>
            <div class="col-sm-9">
              <input type="text" class="form-control" id="price">
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <input type="hidden" id="projectId" name="projectId" />
          <button type="submit" class="btn btn-primary">Submit</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        </div>
      </form>
    </div>
  </div>
</div>
<script src="/libs/bootstrap-datetimepicker/js/moment.js"></script>
<script src="/libs/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script>
  $(function() {
    $('#startDatePicker').datetimepicker();
    $('#endDatePicker').datetimepicker();
  });
</script>