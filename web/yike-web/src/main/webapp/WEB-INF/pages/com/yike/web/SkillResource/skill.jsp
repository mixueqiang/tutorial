<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>${skill.name}</title>

<div class="container row-space-top-2">
  <div class="row">
    <div class="col-md-3 col-sm-3 col-xs-2">
      <a class="btn btn-default" href="/skill">所有技能</a>
    </div>
    <div class="col-md-6 col-sm-6 col-xs-8 card">
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
        <div class="card last">
          <div class="title xl">共享学习资料</div>
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
        <div class="col-md-2 col-sm-3 col-xs-6 row-space-top-2" data-id="${item.id}" data-url="${item.url}">
          <a class="green" href="/resource/${item.id}" target="_blank">
            <div class="card item">
              <div class="title md">${item.content}</div>
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

<div class="modal fade" id="ExchangeModal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title">共享资料</h4>
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
                <input type="text" id="phone" name="phone" tabindex="1" class="form-control">
              </div>
            </div>
            <div class="form-group">
              <label for="securityCode" class="col-sm-3 control-label">验证码</label>
              <div class="col-sm-2">
                <input type="text" id="securityCode" name="securityCode" tabindex="2" class="form-control">
              </div>
              <div class="col-sm-3">
                <button class="btn btn-warning btn-block btn-send-sms">获取验证码</button>
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
      <div class="exchange-info">
        <div class="modal-body">
          <div class="alipay-info"></div>
        </div>
      </div>
    </div>
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
            <label for="content">资料内容介绍</label>
            <textarea class="form-control" id="content" name="content"></textarea>
          </div>
          <div class="form-group">
            <label for="url">百度网盘或URL地址</label> <input class="form-control" id="url" name="url">
          </div>
          <div class="form-group">
            <label for="contact">联系方式（建议留QQ群或微信群）</label><input type="text" class="form-control" id="contact" name="contact">
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

<script src="/js/skill.js?v=20170228001"></script>