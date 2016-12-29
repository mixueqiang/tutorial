<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<div class="container">
  <div class="row row-space-top-4">
    <div class="col-md-9 col-sm-9 col-xs-12" id="courses">
      <div class="section row-space-top-3">
        <c:forEach var="item" items="${courses}">
          <div class="panel panel-default section-course" data-id="${item.id}">
            <div class="panel-body">
              <div class="row">
                <div class="col-sx-8 col-sm-4 col-4">
                  <c:choose>
                    <c:when test="${not empty item.image}">
                      <img alt="翻译资源网-课程" src="http://transkip.b0.upaiyun.com/${item.image}!M">
                    </c:when>
                    <c:otherwise>
                      <img alt="翻译资源网-课程" src="http://transkip.b0.upaiyun.com/201612/course-cover.png!M">
                    </c:otherwise>
                  </c:choose>
                </div>
                <div class="col-sx-8 col-sm-8 col-8">
                  <div class="row row-top">
                    <div class="col-md-8 col-sm-8 col-xs-12">
                      <a class="courses-title" href="/course/${item.id}" target="_blank">${item.name}</a>
                    </div>
                  </div>
                  <a class="courses-name" href="/instructor/${item.properties.instructor.id}" target="_blank">${item.properties.instructor.name}</a> <a id="contentStr" class="courses-content"
                    href="/course/${item.id}" target="_blank">${item.content}</a>
                  <div class="courses-hint row-space-top-1">
                    <span id="courses-subscript">${item.subscript}</span> <span class="courses-price"><c:choose>
                        <c:when test="${item.free eq 1}">免费</c:when>
                        <c:otherwise>¥${item.price}</c:otherwise>
                      </c:choose></span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>

        <ul class="pagination">
          <c:if test="${currentPage ne 1}">
            <li><a href="/course">&laquo;</a></li>
          </c:if>
          <c:forEach var="p" items="${pages}">
            <c:choose>
              <c:when test="${p ne currentPage}">
                <li><a href="/course?p=${p}">${p}</a></li>
              </c:when>
              <c:otherwise>
                <li class="active"><a href="#">${p}</a></li>
              </c:otherwise>
            </c:choose>
          </c:forEach>
          <c:if test="${lastPage > 0 and currentPage ne lastPage}">
            <li><a href="/course?p=${lastPage}">&raquo;</a></li>
          </c:if>
        </ul>
      </div>
    </div>

    <div class="col-md-3 col-sm-3 col-xs-6">
      <div class="section row-space-top-3">
        <a class="btn btn-warning btn-block" href="/courses" target="_blank">我的课程</a>
      </div>
    </div>

  </div>
</div>

<script src="/js/article.js?v=20161021001"></script>
<script>
	var constr = document.getElementById('contentStr');
	var str = constr.innerHTML;
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

	constr.innerHTML = cutStr(str, 190);
</script>