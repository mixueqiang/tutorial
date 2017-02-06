<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="marketing-banner hero-top">
  <div class="hero-header">
    <div class="container">
      <a class="hero-brand xl" href="/">一课上手</a>
      <ul class="nav pull-right">
        <li class="pull-left"><a id="publishTop" class="btn btn-success" href="/course/create">发布一门实践课程</a></li>
        <c:choose>
          <c:when test="${not empty _user}">
            <div class="dropdown headerDropdown pull-left">
              <button class="btn dropdown-toggle dropdownMenu1" type="button" id="dropdownMenu1" data-toggle="dropdown">
                <a href="/dashboard">${_user.username}</a><span class="caret"></span>
              </button>
              <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                <li role="presentation"><a role="menuitem" tabindex="-1" href="/courses">我的课程</a></li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="/settings/profile">个人信息</a></li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="/signout">退出登录</a></li>
              </ul>
            </div>
          </c:when>
          <c:otherwise>
            <li class="pull-left col-space-2"><a href="/signin">登录</a></li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </div>
  <div class="hero-overlay"></div>
  <div class="intro-area text-center">
    <div class="col-middle content">
      <h1>学习，并且实战</h1>
      <h2 class="subtitle"></h2>
      <a id="signup" class="btn btn-success row-space-top-6" href="/signup" target="_blank">注册</a>
    </div>
  </div>
</div>

<div class="marketing-course index-bgcolor">
  <div class="container">
    <div class="row">
      <div class="col-md-12">
        <c:forEach var="item" items="${categories}">
          <div class="row">
            <div class="col-md-12">
              <div class="title">
                <div class="lg pull-left">${item.name}</div>
                <div class="md pull-right">
                  <a href="/course?c=${item.id}">更多课程></a>
                </div>
              </div>
            </div>
          </div>
          <div class="row row-space-top-1 list">
            <c:forEach var="item" items="${courses[item.id]}">
              <div class="col-md-3 col-sm-6 index-panel index-bgcolor">
                <div class="index-panel-body">
                  <div class="section">
                    <div class="section-cover">
                      <c:choose>
                        <c:when test="${not empty item.image}">
                          <a href="/course/${item.id}"><div class="imgadapt"
                              style=" width:100%; background: url('http://yikeyun.b0.upaiyun.com/${item.image}!M') 0 0 no-repeat; background-size:cover;"></div></a>
                        </c:when>
                        <c:otherwise>
                          <a href="/course/${item.id}"><div class="imgadapt"
                              style="width: 100%; background: url('http://yikeyun.b0.upaiyun.com/static/course-cover.png!M') 0 0 no-repeat; background-size: cover;"></div></a>
                        </c:otherwise>
                      </c:choose>
                    </div>
                    <div class="section-contents index-contents">
                      <div class="section-title index-title">
                        <a href="/course/${item.id}" target="_blank">${item.name}</a>
                      </div>
                      <!-- <div class="section-caption row-space sm">${item.properties.instructor.name}</div> -->
                      <div class="section-content index-content ln-1">${item.content}</div>
                      <div class="section-footer">
                        <span class="section-subscript">${item.superscript}</span><span class="section-price pull-right">¥${item.price}</span>
                      </div>
                    </div>

                  </div>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>

<div class="marketing-banner hero-bottom">
  <div class="hero-overlay"></div>
  <div class="intro-area">
    <div class="col-middle content">
      <h1>每位有工作经验的人都是老师</h1>
      <h2 class="subtitle"></h2>
      <a id="publishBottom" class="btn btn-success row-space-top-6" href="/course/create">发布一门实践课程</a>
    </div>
  </div>
</div>
<div id="aboutUs" class="container">
  <div class="row">
    <div class="about col-xs-12 col-sm-12 col-md-5 col-lg-5">
      <h1>关于我们</h1>
      <p>“ 一课上手 ” 是一个专注实践类课程的平台。在一课，你可以通过实践课程参与到真实的项目中，收获到真实的动手经验。我们致力于把真实的经验和实战带给学员，也希望学员能通过老师分享的经验和实战把学到的东西快速上手。术业专攻，一课上手。</p>
      <p>在一课，经验和实战是最大的老师。每一位有经验和实战的人都是我们在寻找的好老师。如果你在某一专业上有实战经验，联系我们，我们一起让经验和实战发挥更大的作用！我们诚挚的邀请各位有行业经验的老师来发挥你的才智。</p>
    </div>
    <div id="contact" class="contact col-xs-12 col-sm-12 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1">
      <h1>为你设计</h1>
      <p>每一个有工作经验的人都可以讲课，告诉我们你的工作经历，让我们和你一起设计一门实践课程。</p>
      <form class="text-left" id="contact_form" action="/api/v1/invitation" method="post" class="form-inline" role="form">
        <div class="form-group">
          <input id="name" type="text" name="name" placeholder="姓名">
        </div>
        <div class="form-group">
          <input id="contacts" type="text" name="contacts" placeholder="手机号码">
        </div>
        <div class="form-group">
          <textarea id="content" name="content" rows="3" placeholder="工作经历"></textarea>
        </div>
        <button id="btn" type="submit" class="btn btn-success">提交我的经验</button>
      </form>
    </div>
  </div>
</div>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title" id="myModalLabel">反馈</h4>
      </div>
      <div class="modal-body"></div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal">好的</button>
      </div>
    </div>
  </div>
</div>
<script>
/*图片高度适配*/
function Heightadapt(){
  $('.section-cover .imgadapt').height($('.section-cover .imgadapt').width()*0.62);
}
Heightadapt();
window.onresize=function(){
  Heightadapt();
}
/* 字数控制事件 */
 /**  
  * js截取字符串，中英文都能用  
  * @param str：需要截取的字符串  
  * @param len: 需要截取的长度  
  */
 function cutStr(str, len) {
   var str_length = 0;
   var str_len = 0;
   str_cut = new String();
   str_len = str.length;
   for (var i = 0; i < str_len; i++) {
     a = str.charAt(i);
     str_length++;
     if (escape(a).length > 4) {
       //中文字符的长度经编码之后大于4    
       str_length++;
     }
     str_cut = str_cut.concat(a);
     if (str_length >= len) {
       str_cut = str_cut.concat("...");
       return str_cut;
     }
   }
   if (str_length < len) {
     return str;
   }
 }

 for (var i = 0; i < $('.list .section-content').length; i++) {
    $('.list .section-content').eq(i).html(cutStr($('.list .section-content').eq(i).text(), 88));
    $('.list .section-title a').eq(i).html(cutStr($('.list .section-title a').eq(i).text(), 50));
 }
/*表单验证事件*/
	$('#contact_form').validate({
		rules : {
			name : {
				required : true,
			},
			contacts : {
				required : true,
				number:true,
				rangelength:[11,11]
			},
			content : {
				required : true,
			},

		},
		messages : {
			name : {
				required : '请输入你的名字',
			},
			contacts : {
				required : '请输入联系方式',
				number: "请输入正确格式手机号",
				rangelength:"请输入正确格式手机号"
			},
			content : {
				required : '请输入你的工作经历',
			},

		},
		submitHandler : function(form) {
			var $btn = $('button[type=submit]', $(form));
			$btn.attr('disabled', 'disabled').addClass('disabled');
			if (!$(form).valid()) {
				$('.error').eq(0).focus();
				$btn.removeAttr('disabled').removeClass('disabled');
				return false;
			}
			$(form).ajaxSubmit({
				success : function(resp) {
					if (resp && resp.e == 0) {
						$('input,textarea', $(form)).val('');
						$btn.removeAttr('disabled').removeClass('disabled');
						var id = resp.r;
             $('.modal-body').html('感谢你的提交，我们会很快和你取得联系。');
            $('#myModal').modal('show');
						/*alert('感谢你的提交，我们会很快和你取得联系。');*/
					} else {
						$btn.removeAttr('disabled').removeClass('disabled');
            $('.modal-body').html('信息提交失败:' + resp.m);
            $('#myModal').modal('show');
						/*alert('信息提交失败:' + resp.m);*/
					}
				},
				error : function() {
					$btn.removeAttr('disabled').removeClass('disabled');
          $('.modal-body').html('信息提交失败:' + resp.m);
          $('#myModal').modal('show');
					/*alert('信息提交失败:' + resp.m);*/
				}
			});
		}
	});

	/* 不登陆状态，发布课程跳到底部 ;登陆状态 跳转道发布课程页*/
	$('#publishTop').click(function(){
		if(${isLogin}==false){/* 没登陆 */
		    $("html,body").animate({scrollTop:$('#contact').get(0).offsetTop}, 500);
		    $('#publishTop').attr("href","#");
		  }else{
		    $('#publishTop').attr("href","/course/create");
		  }
	});

	$('#publishBottom').click(function(){
    if(${isLogin}==false){/* 没登陆 */
        $("html,body").animate({scrollTop:$('#contact').get(0).offsetTop}, 500);
        $('#publishBottom').attr("href","#");
      }else{
        $('#publishBottom').attr("href","/course/create");
      }
  });
  
  /* 对于不登录状态 注册显示 */
  if(${isLogin}==true){
    $('#signup').attr("href","/course");
    $('#signup').html('全部课程');
  }
</script>