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
          <div class="card item" data-id="${item.id}" data-source="${item.source}" data-target="${item.target}">
            <div class="title xl">${item.contact}</div>
            <div class="md">需要${item.targetFu}</div>
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
        <h4 class="modal-title">换福</h4>
      </div>
      <div class="exchange-check">
        <div class="modal-body">
          <div class="row loading">
            <div class="col-md-3 col-md-offset-4 col-sm-4 col-sm-offset-4 col-xs-6 col-xs-offset-3">
              <img src="/images/loading.gif">
            </div>
          </div>
        </div>
      </div>
      <div class="exchange-signup" style="display: none;">
        <form id="signup-form" name="signup-form" action="/fu/user" method="post" class="form-horizontal" role="form">
          <div class="modal-body">
            <div class="form-group">
              <label for="phone" class="col-sm-3 control-label">手机号</label>
              <div class="col-sm-5">
                <input type="text" id="phone" name="phone" tabindex="1" class="form-control" />
              </div>
            </div>
            <div class="form-group">
              <label for="securityCode" class="col-sm-3 control-label">验证码</label>
              <div class="col-sm-2">
                <input type="text" id="securityCode" name="securityCode" tabindex="2" class="form-control" />
              </div>
              <div class="col-sm-3">
                <button class="btn btn-warning btn-block btn-send-sms">获取验证码</button>
              </div>
            </div>
            <div class="form-group">
              <label for="password" class="col-sm-3 control-label">密码</label>
              <div class="col-sm-5">
                <input type="password" id="password" name="password" tabindex="3" class="form-control" />
              </div>
            </div>
            <div class="form-group">
              <div class="col-sm-4 col-sm-offset-3">
                <input type="hidden" id="exchangeId" name="exchangeId">
                <button type="submit" class="btn btn-success">提交，获取换福信息</button>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="exchange-check">
        <div class="modal-body">
          <div class="alipay_info"></div>
        </div>
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