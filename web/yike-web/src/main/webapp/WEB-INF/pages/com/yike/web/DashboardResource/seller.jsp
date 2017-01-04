<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="page-header">
        <h4 id="project-title">Projects - Translator View</h4>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-10 col-md-offset-1">
      <!-- Nav tabs -->
      <ul class="nav nav-tabs">
        <li class="active"><a href="#active" data-toggle="tab">Active Bids</a></li>
        <li><a href="#work" data-toggle="tab">Current Work</a></li>
        <li><a href="#past" data-toggle="tab">Past Work</a></li>
      </ul>

      <!-- Tab panes -->
      <div class="tab-content">
        <div class="tab-pane active" id="open">
          <table class="table" id="projects">
            <thead>
              <tr>
                <th>Project Name</th>
                <th>Employer</th>
                <th>Bids</th>
                <th>Avg Bid</th>
                <th>My Bid</th>
                <th>Bid End Date</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item" varStatus="status" items="${activeProjects}">
                <tr data-id="${item.id}">
                  <td><a href="/project/${item.id}">${item.name}</a></td>
                  <td>${item.userId}</td>
                  <td>0</td>
                  <td>$0.0</td>
                  <td>$0.0</td>
                  <td><jsp:useBean id="startDate" class="java.util.Date" /><jsp:setProperty name="startDate" property="time" value="${item.createTime}" /> <fmt:formatDate value="${startDate}"
                      pattern="yyyy-MM-dd" timeZone="GMT+0000" /></td>
                  <td></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
        <div class="tab-pane" id="work">
          <table class="table" id="projects">
            <thead>
              <tr>
                <th>Project Name</th>
                <th>Employer</th>
                <th>Awarded Bid</th>
                <th>Deadline</th>
                <th>Milestones</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item" varStatus="status" items="${workProjects}">
                <tr data-id="${item.id}">
                  <td><a href="/project/${item.id}">${item.name}</a></td>
                  <td></td>
                  <td>$0.0</td>
                  <td></td>
                  <td>$0.0</td>
                  <td></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
        <div class="tab-pane" id="past">
          <table class="table" id="projects">
            <thead>
              <tr>
                <th>Project Name</th>
                <th>Employer</th>
                <th>Bids</th>
                <th>Awarded Bid</th>
                <th>Time</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item" varStatus="status" items="${pastProjects}">
                <tr data-id="${item.id}">
                  <td><a href="/project/${item.id}">${item.name}</a></td>
                  <td></td>
                  <td></td>
                  <td>$0.0</td>
                  <td></td>
                  <td></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>