;(function($) {
  var _document = $(document);

  /**
   * 削除
   */
  _document.on('submit', '#admin-delete-contribution', function(event) {
    event.preventDefault();
    var _this = $(this);
    var jsondata = {
      id: _this.find('.delete-id').val().json_escape()
    };

    $.ajax({
      url: '/api/admin_delete',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(jsondata),
    })
    .done(function() {
      console.log("success");
      _this.parents('.contribution').fadeOut(400, function(){
        $(this).remove();
      });
    })
    .fail(function() {
      console.log("error");
      noticeError("投稿の削除");
    });

  });

  /**
   * エラー表示
   */
  var noticeError = function(text) {
    $error = $('.notice-error');

    $error.find('.error-text').text(text);
    $error.addClass('active').css({
        top: 0,
        opacity: 1
      });
    setTimeout(function() {
      $error.css({
        top: '-70px',
        opacity: 0
      }).removeClass('active');
    }, 3000);
  }

  /**
   * 文字列をエスケープする
   */
  String.prototype.json_escape = function() {
    return ("" + this)
      .replace(/\\/g, "\\\\")
      .replace(/\"/g, "\\\"")
      .replace(/\//g, "\\\/")
      .replace(/\W/g, function (c) {
      return "\\u" + ("000" + c.charCodeAt(0).toString(16)).slice(-4);
    });
  }

}(jQuery));