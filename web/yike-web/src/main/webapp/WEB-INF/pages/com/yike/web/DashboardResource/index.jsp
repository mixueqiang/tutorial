<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>

<div class="container">
  <div class="row">
    <div class="col-md-3 db-sidebar">
      <ul>
        <li><strong>用户信息</strong></li>
        <li><a href="/user">基本信息</a></li>
        <li><a href="/user/privacy_options">隐私设置</a></li>
        <li><a href="/password/change">密码修改</a></li>
      </ul>
      <ul>
        <li><strong>翻译服务</strong></li>
        <!-- <li><span class="glyphicon glyphicon-hand-right"></span>&nbsp;<a href="/dashboard/buyer">I am project owner</a></li>
        <li><span class="glyphicon glyphicon-hand-right"></span>&nbsp;<a href="/dashboard/seller">I am translator</a></li> -->
        <li><a href="/dashboard/worker">我是译员</a></li>
        <li><a href="/dashboard/company">我是翻译公司</a></li>
        <li><a href="/dashboard/client">我是翻译客户</a></li>
      </ul>
    </div>
    <div class="col-md-8 db-main"></div>
  </div>
</div>