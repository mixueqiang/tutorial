<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>${skill.name}</title>

<div class="container row-space-top-2">
  <div class="row">
    <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 col-xs-8 col-xs-offset-2 card">
      <div class="card-cover">
        <c:if test="${not empty skill.image}">
          <img alt="技能图标" src="http://yikeyun.b0.upaiyun.com${skill.image}!M">
        </c:if>
      </div>
      <div class="card-title">
        <span class="xl">${skill.name}</span>
      </div>
      <div class="card-attribute hide">
        <dl>
          <dt>公司</dt>
          <dd>${skill.companyCount}</dd>
        </dl>
        <dl class="col-space-6">
          <dt>用户</dt>
          <dd>${skill.userCount}</dd>
        </dl>
      </div>
    </div>
  </div>

  <div class="row row-space-top-1 section skill-section">
    <div class="col-md-2 col-sm-3 col-xs-6 row-space-top-2">
      <a class="green op-publish" data-toggle="modal" data-target="#PublishModal" href="#" title="共享学习资料">
        <div class="card item item-lg">
          <div class="title md">共享学习资料</div>
          <div class="sm"></div>
          <div class="card-footer">
            <div class="card-action xl">
              <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
            </div>
          </div>
        </div>
      </a>
    </div>
    <c:if test="${not empty resources}">
      <c:forEach var="item" items="${resources}">
        <div class="col-md-2 col-sm-3 col-xs-6 row-space-top-2" data-id="${item.id}">
          <a class="green" href="/resource/${item.id}" target="_blank">
            <div class="card item item-lg item-green">
              <div class="title md">${item.title}</div>
              <div class="sm">${item.contact}</div>
              <div class="card-footer">
                <div class="card-action xl">
                  <span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span>
                </div>
              </div>
            </div>
          </a>
        </div>
      </c:forEach>
    </c:if>
  </div>
</div>

<div class="modal fade" id="PublishModal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-md" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title">共享学习资料</h4>
      </div>
      <form action="/resource" id="publish-form" method="post">
        <div class="modal-body">
          <div class="form-group">
            <label for="title">名称</label><input class="form-control" id="title" name="title">
          </div>
          <div class="form-group">
            <label for="content">资料介绍</label>
            <textarea class="form-control" id="content" name="content" placeholder="可选" rows="2"></textarea>
          </div>
          <div class="form-group">
            <label for="url">百度网盘或网址</label><input class="form-control" id="url" name="url">
          </div>
          <div class="form-group">
            <label for="password">访问密码</label><input class="form-control" id="password" name="password" placeholder="可选">
          </div>
        </div>
        <div class="modal-footer">
          <input type="hidden" name="skillId" value="${skill.id}">
          <button type="submit" class="btn btn-success">提交</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="/js/resource.js?v=20170314001"></script>