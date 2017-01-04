<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container">
  <div class="row">
    <div class="col-md-12">
      <div class="page-header">
        <h4 id="project-title">Projects - Project Owner View</h4>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-10 col-md-offset-1">
      <!-- Nav tabs -->
      <ul class="nav nav-tabs">
        <li class="active"><a href="#open" data-toggle="tab">Open for Bidding</a></li>
        <li><a href="#work" data-toggle="tab">Work in progress</a></li>
        <li><a href="#past" data-toggle="tab">Past Projects</a></li>
      </ul>

      <!-- Tab panes -->
      <div class="tab-content">
        <div class="tab-pane active" id="open">
          <table class="table" id="projects">
            <thead>
              <tr>
                <th>Project Name</th>
                <th>Bids</th>
                <th>Avg Bid (USD)</th>
                <th>Bid Start Date</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item" varStatus="status" items="${openProjects}">
                <tr data-id="${item.id}">
                  <td style="max-width: 400px;"><a href="/project/${item.id}">${item.name}</a></td>
                  <td>${item.bids}</td>
                  <td>$${item.averageBid}</td>
                  <td><jsp:useBean id="startDate" class="java.util.Date" /><jsp:setProperty name="startDate" property="time" value="${item.createTime}" /> <fmt:formatDate value="${startDate}"
                      pattern="yyyy-MM-dd" timeZone="GMT+0000" /></td>
                  <td><a href="/project/${item.id}#project-bids">Check Bids</a></td>
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
                <th>Translator</th>
                <th>Awarded Bid (USD)</th>
                <th>Deadline</th>
                <th>Milestones</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item_c" varStatus="status" items="${workProjects}">
                <tr data-id="${item_c.id}">
                  <td style="max-width: 400px;"><a href="/project/${item_c.id}">${item_c.name}</a></td>
                  <td>${item_c.bid.user.username}</td>
                  <td>$${item_c.bid.awardedBid}</td>
                  <td></td>
                  <td>$${item_c.bid.milestone}</td>
                  <td><a href="/dashboard/project?project_id=${item_c.id}">Manage</a></td>
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
                <th>Awarded Bid (USD)</th>
                <th>Time</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item_p" varStatus="status" items="${pastProjects}">
                <tr data-id="${item_p.id}">
                  <td style="max-width: 400px;"><a href="/project/${item_p.id}">${item_p.name}</a></td>
                  <td>$${item_p.bid.awardedBid}</td>
                  <td><jsp:useBean id="item_p_date" class="java.util.Date" /><jsp:setProperty name="item_p_date" property="time" value="${item_p.createTime}" /> <fmt:formatDate
                      value="${item_p_date}" pattern="yyyy-MM-dd" timeZone="GMT+0000" /></td>
                  <td>${item_p.status}</td>
                  <td><a href="/dashboard/project?project_id=${item_p.id}">View</a></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>