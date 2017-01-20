<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>${skill.name}</title>

<div class="container row-space-top-2">
  <div class="row">
    <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 col-xs-8 col-xs-offset-2 card">
      <div class="card-cover">
        <c:if test="${not empty skill.image}">
          <img alt="技能图标" src="http://yikeyun.b0.upaiyun.com/${skill.image}!M">
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
    <c:if test="${not empty entities}">
      <div class="col-md-2 col-sm-3 col-xs-6 row-space-top-2">
        <div class="card first">
          <div class="title xl">{谁}有</div>
          <div class="md">TA需要{什么}</div>
          <div class="card-footer">
            <div class="card-action md">
              <span class="glyphicon glyphicon-transfer" aria-hidden="true"></span><span>点击交换</span>
            </div>
          </div>
        </div>
      </div>
      <c:forEach var="item" items="${entities}">
        <div class="col-md-2 col-sm-3 col-xs-6 row-space-top-2">
          <div class="card item">
            <div class="title xl">${item.contact}</div>
            <div class="md"></div>
            <div class="card-footer">
              <div class="card-action xxl">
                <a class="green op-exchange" href="#" title="换福"><span class="glyphicon glyphicon-transfer" aria-hidden="true"></span></a>
              </div>
            </div>
          </div>
        </div>
      </c:forEach>
    </c:if>
    <div class="col-md-2 col-sm-3 col-xs-6 row-space-top-2">
      <div class="card last">
        <div class="title xl">我有福</div>
        <div class="md"></div>
        <div class="card-footer">
          <div class="card-action xxl">
            <a class="green op-publish" data-toggle="modal" data-target="#PublishModal" href="#" title="发布一张福"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></a>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="row row-space-top-6">
    <div class="col-md-2 col-md-offset-5 col-sm-4 col-sm-offset-4 col-xs-6 col-xs-offset-3">
      <a class="btn btn-default btn-block" href="/fu">返回首页</a>
    </div>
  </div>

</div>

<div class="modal fade" id="ExchangeModal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title"></h4>
      </div>
      <div class="modal-body">
        <p>One fine body&hellip;</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="PublishModal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title">换福</h4>
      </div>
      <form action="/fu/publish" id="publish-form" method="post">
        <div class="modal-body">
          <div class="form-group">
            <label for="source">我有</label><select class="form-control" id="source" name="source">
              <option value="1001" <c:if test="${skill.id eq 1001}">selected="selected"</c:if>>爱国福</option>
              <option value="1002" <c:if test="${skill.id eq 1002}">selected="selected"</c:if>>富强福</option>
              <option value="1003" <c:if test="${skill.id eq 1003}">selected="selected"</c:if>>和谐福</option>
              <option value="1004" <c:if test="${skill.id eq 1004}">selected="selected"</c:if>>友善福</option>
              <option value="1005" <c:if test="${skill.id eq 1005}">selected="selected"</c:if>>敬业福</option>
            </select>
          </div>
          <div class="form-group">
            <label for="target">我想要</label><select class="form-control" id="target" name="target">
              <option value="1001">爱国福</option>
              <option value="1002">富强福</option>
              <option value="1003">和谐福</option>
              <option value="1004">友善福</option>
              <option value="1005">敬业福</option>
            </select>
          </div>
          <div class="form-group">
            <label for="contact">手机号或QQ号</label><input type="text" class="form-control" id="contact" name="contact">
          </div>
          <div class="form-group">
            <label for="alipay">留下支付宝，方便交换福</label><input type="text" class="form-control" id="alipay" name="alipay">
          </div>
          <div class="form-group">
            <label for="">或加入QQ群：612511614换福</label>
          </div>
        </div>
        <div class="modal-footer">
          <input type="submit" class="btn btn-success" value="提交">
          <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="/js/fu.js?v=20170120001"></script>