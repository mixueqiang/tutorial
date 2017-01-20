<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>集福</title>

<div class="container row-space-top-4">
  <div class="row">
    <div class="col-md-8 col-md-offset-2 col-sm-12">
      <div class="row hide">
        <div class="col-sm-12 col-xs-12">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title">我可以交换的福</h3>
            </div>
            <form action="/fu/submit" class="form-horizontal" method="post">
              <div class="panel-body">
                <div class="form-group">
                  <div class="col-sm-4 col-xs-4">
                    <select class="form-control" id="fu_aiguo" name="fu_aiguo">
                      <option value="0">爱国</option>
                      <option value="0">0个</option>
                      <option value="1">1个</option>
                      <option value="2">2个</option>
                      <option value="3">3个</option>
                      <option value="4">4个</option>
                      <option value="5">5个</option>
                    </select>
                  </div>
                  <div class="col-sm-4 col-xs-4">
                    <select class="form-control" id="fu_fuqiang" name="fu_fuqiang">
                      <option value="0">富强</option>
                      <option value="0">0个</option>
                      <option value="1">1个</option>
                      <option value="2">2个</option>
                      <option value="3">3个</option>
                      <option value="4">4个</option>
                      <option value="5">5个</option>
                    </select>
                  </div>
                  <div class="col-sm-4 col-xs-4">
                    <select class="form-control" id="fu_hexie" name="fu_hexie">
                      <option value="0">和谐</option>
                      <option value="0">0个</option>
                      <option value="1">1个</option>
                      <option value="2">2个</option>
                      <option value="3">3个</option>
                      <option value="4">4个</option>
                      <option value="5">5个</option>
                    </select>
                  </div>
                </div>
                <div class="form-group">
                  <div class="col-sm-4 col-xs-4">
                    <select class="form-control" id="fu_youshan" name="fu_youshan">
                      <option value="0">友善</option>
                      <option value="0">0个</option>
                      <option value="1">1个</option>
                      <option value="2">2个</option>
                      <option value="3">3个</option>
                      <option value="4">4个</option>
                      <option value="5">5个</option>
                    </select>
                  </div>
                  <div class="col-sm-4 col-xs-4">
                    <select class="form-control" id="fu_jingye" name="fu_jingye">
                      <option value="0">敬业</option>
                      <option value="0">0个</option>
                      <option value="1">1个</option>
                      <option value="2">2个</option>
                      <option value="3">3个</option>
                      <option value="4">4个</option>
                      <option value="5">5个</option>
                    </select>
                  </div>
                  <div class="col-sm-4 col-xs-4">
                    <input class="btn btn-success btn-block" type="submit" value="提交">
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-sm-4 col-xs-12">
          <div class="panel panel-default" id="fu1">
            <div class="panel-heading">
              <h3 class="panel-title">爱国福</h3>
            </div>
            <div class="panel-body">
              <img src="http://yikeyun.b0.upaiyun.com/static/fu1.png!M">
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_aiguo">交换</a>
            </div>
          </div>
        </div>
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu2">
            <div class="panel-heading">
              <h3 class="panel-title">富强福</h3>
            </div>
            <div class="panel-body">
              <img src="http://yikeyun.b0.upaiyun.com/static/fu2.png!M">
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_fuqiang">交换</a>
            </div>
          </div>
        </div>
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu3">
            <div class="panel-heading">
              <h3 class="panel-title">和谐福</h3>
            </div>
            <div class="panel-body">
              <img src="http://yikeyun.b0.upaiyun.com/static/fu3.png!M">
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_hexie">交换</a>
            </div>
          </div>
        </div>
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu4">
            <div class="panel-heading">
              <h3 class="panel-title">友善福</h3>
            </div>
            <div class="panel-body">
              <img src="http://yikeyun.b0.upaiyun.com/static/fu4.png!M">
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_youshan">交换</a>
            </div>
          </div>
        </div>
        <div class="col-sm-4 col-xs-6">
          <div class="panel panel-default" id="fu5">
            <div class="panel-heading">
              <h3 class="panel-title">敬业福</h3>
            </div>
            <div class="panel-body">
              <img src="http://yikeyun.b0.upaiyun.com/static/fu1.png!M">
            </div>
            <div class="panel-footer">
              <a class="btn btn-success btn-block" href="/skill/fu_jingye">交换</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>