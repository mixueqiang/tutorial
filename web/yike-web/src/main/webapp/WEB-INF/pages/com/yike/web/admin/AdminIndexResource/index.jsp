<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>

<div class="container">
  <div class="row row-space-top-4">
    <div class="col-md-4 col-sm-6 col-xs-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <ul>
            <li><strong>网站</strong></li>
            <li><a href="/admin/user" target="_blank">用户 (${userCount}人)</a></li>
            <li><a href="/admin/shorthand/new_instructor" target="_blank">快速注册老师</a></li>
            <li><a href="/admin/course" target="_blank">课程列表</a></li>
            <li><a href="/admin/schedule" target="_blank">课程表</a></li>
            <li><a href="/admin/order" target="_blank">课程订单</a></li>
            <li><a href="/admin/resource" target="_blank">资料资源</a></li>
          </ul>
        </div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6 col-xs-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <ul>
            <li><strong>公众号</strong></li>
            <li><a href="/admin/wxuser/it" target="_blank">IT技能成长联盟公众号-用户 (${itUserCount}人)</a></li>
            <li><a href="/admin/wxtextresponse/it" target="_blank">IT技能成长联盟公众号-自动回复内容</a></li>
            <li><a href="/admin/wxuser/yike" target="_blank">一课上手公众号-用户 (${yikeUserCount}人)</a></li>
            <li><a href="/admin/wxtextresponse/yike" target="_blank">一课上手公众号-自动回复内容</a></li>
          </ul>
        </div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6 col-xs-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <ul>
            <li><strong>研发／设计／文档</strong></li>
            <li><a href="http://www.wfenxiang.com/" target="_blank">设计原型</a></li>
            <li><a href="https://github.com/mixueqiang/yike/wiki" target="_blank">开发文档</a></li>
            <li><a href="https://github.com/mixueqiang/yike" target="_blank">代码仓库Yikeshangshou</a></li>
            <li><a href="https://github.com/mixueqiang/simpleworks" target="_blank">代码仓库Simpleworks</a></li>
          </ul>
        </div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6 col-xs-12">
      <div class="panel panel-default">
        <div class="panel-body">
          <ul>
            <li><strong>工具</strong></li>
            <li><a href="/admin/image" target="_blank">图片上传</a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>