<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>上传一张图片 | 翻译资源网</title>

<div class="container">
  <div class="row">
    <div class="col-md-12">
      <h2>上传一张图片</h2>
      <div class="section">
        <div class="form-group">
          <div class="col-md-12">
            <span class="btn btn-default file-input-container"> 从电脑选择并上传图片 <input type="file" name="imageFile" class="fileupload"></span>
          </div>
        </div>
        <div class="form-group">
          <div class="col-md-6 col-sm-8 col-xs-12 image-container"></div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="/libs/jquery/jquery.ui.widget.js"></script>
<script src="/libs/jquery/jquery.iframe-transport.js"></script>
<script src="/libs/jquery/jquery.fileupload.js"></script>
<script>
  //上传图片处理
  $('.fileupload').fileupload({
    url : '/api/v1/image',
    dataType : 'json',
    add : function(e, data) {
      data.submit();
    },
    done : function(e, data) {
      var resp = data.result;
      if (resp && resp.e == 0) {
        var image = resp.r;
        $('.image-container').html('<p><input type="text" class="form-control" value="' + image.path + '"></p><p><img alt="翻译资源网-图片" src="' + image.url + '"></p>');
      }
    },
    progressall : function(e, data) {
      // update progress.
      var progress = parseInt(data.loaded / data.total * 100, 10);
      $('#progress .progress-bar').css('width', progress + '%');
    }
  }).prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
</script>