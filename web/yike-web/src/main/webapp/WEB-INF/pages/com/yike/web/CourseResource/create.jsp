<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>发布实战课程&nbsp;|&nbsp;一课上手</title>
<div class="container">
  <div class="row">
    <div class="col-md-10 col-md-offset-1 col-sm-12 col-xs-12">
      <h2>发布实战课程</h2>
      <form id="courseForm" name="course-form" action="/api/v1/course" method="post" class="form-horizontal row-space-top-4" role="form">
        <div class="form-group">
          <label for="title" class="col-md-2 col-sm-2 control-label">课程名称</label>
          <div id="titleContainer" class="col-md-8 col-sm-8">
            <p id="titleNum"></p>
            <input id="title" name="title" class="form-control" maxlength="500">
          </div>
        </div>
        <div class="form-group">
          <label for="content" class="col-md-2 col-sm-2 control-label">课程亮点</label>
          <div id="contentContainer" class="col-md-8 col-sm-8">
            <p id="contentNum"></p>
            <textarea contenteditable="true" id="content" name="content" rows="3" class="form-control" placeholder=""></textarea>
          </div>
        </div>
        <div class="form-group">
          <label for="price" class="col-md-2 col-sm-2 col-xs-3 control-label">课程价格</label>
          <div class="col-md-2 col-sm-2 col-xs-4">
            <select id="free" name="free" class="form-control">
              <option value="1">免费</option>
              <option value="0">收费</option>
            </select>
          </div>
          <div id="charge" class="col-md-2 col-sm-2 col-xs-4">
            <span class="rmb">元</span> <input id="price" name="price" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="countThis" class="col-md-2 col-sm-2 control-label">招生人数</label>
          <div class="col-md-2 col-sm-2">
            <input id="countThis" name="countThis" class="form-control">
          </div>
        </div>
        <div class="form-group">
          <label for="categoryId" class="col-md-2 col-sm-2 col-xs-3 control-label">课程类别</label>
          <div class="col-md-2 col-sm-2 col-xs-4">
            <select id="categoryId" name="categoryId" class="form-control">
              <c:forEach var="item" items="${categories}">
                <option value="${item.id}">${item.name}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label for="teachingType" class="col-md-2 col-sm-2 control-label">上课时间及方式</label>
          <div id="teachingTypeContainer" class="col-md-8 col-sm-8">
            <p id="teachingTypeNum"></p>
            <textarea id="teachingType" name="teachingType" rows="4" class="form-control" placeholder="描述什么时间上课，及如何上课 "></textarea>
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="col-md-2 col-sm-2 control-label">详细介绍</label>
          <div id="descriptionContainer" class="col-md-8 col-sm-8">
            <p id="descriptionNum"></p>
            <textarea id="description" name="description" class="form-control" rows="6" placeholder="可以描述适合人群，课程目标，课程详细信息等"></textarea>
          </div>
        </div>
        <div class="form-group">
          <div class="col-md-4 col-md-offset-2 col-sm-4 col-sm-offset-2">
            <button type="submit" class="btn btn-danger">发布</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="/js/course.js?v=20161202001"></script>
<script>
  /* 免费/收费-隐藏显示事件 */
  function fee() {
    if ($("#free").val() == 1) {
      $("#charge").hide();
    } else {
      $("#charge").show();
    }
  }
  fee();
  $("#free").change(function() {
    fee();
  });

  /*字数控制事件*/
  function numChange(conId, numId, maxNum) {
    var time = null;
    conId.onfocus = function() {
      time = setInterval(function() {
        var maxLen = maxNum;
        var txtLen = conId.value.length;
        maxLen = maxLen - txtLen;
        if (maxLen < 0) {
          numId.style.color = 'red';
        } else {
          numId.style.color = '#999999';
          contentNum.style.bottom = '1px';
        }
        numId.innerHTML = maxLen;
      }, 30)
    }
    conId.onblur = function() {
      clearInterval(time);
      numId.innerHTML = '';
    }
  }
  numChange(title, titleNum, 30);
  numChange(content, contentNum, 150);
  numChange(teachingType, teachingTypeNum, 2000);
  numChange(description, descriptionNum, 3000);
</script>