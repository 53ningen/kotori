;(function($) {
  var _document = $(document);

  /**
   * 投稿
   */
  _document.on('submit', '#post-contribution', function(event) {
    event.preventDefault();

    var jsondata = {
      username: $('.post-content--username').val().json_escape(),
      title: $('.post-content--title').val().json_escape(),
      content: $('.post-content--text').val().json_escape(),
      deleteKey: $('.post-content--deletekey').val().json_escape()
    };

    $.ajax({
      url: '/api/post',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(jsondata),
    })
    .done(function(data) {
      console.log("success");
      $posts.slideToggle(400, function() {
        $('.post-content--username').val("");
        $('.post-content--title').val("");
        $('.post-content--text').val("");
        $('.post-content--deleteKey').val("");
      });
      $('#contributions').prepend($(createContribution(data)).fadeIn(400));
    })
    .fail(function(data) {
      console.log("error");
      var errorMsg = data.responseText;
      if (errorMsg === "") {
        errorMsg = "新規投稿に失敗しました"
      }
      noticeError(errorMsg);
    });

  });

  /**
   * 削除
   */
  _document.on('submit', '#delete-contribution', function(event) {
    event.preventDefault();
    var _this = $(this);
    var jsondata = {
      id: _this.find('.delete-id').val().json_escape(),
      username: _this.find('.delete-username').val().json_escape(),
      deleteKey: _this.find('.delete-key').val().json_escape(),
    };

    $.ajax({
      url: '/api/delete',
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
      noticeError("投稿の削除に失敗しました");
      _this.find('.delete-key').val("");
    });

  });

  /**
   * 検索
   */
  _document.on('submit', '#search-contribution', function(event) {
    event.preventDefault();

    var word = $('.search-content--word').val();
    $(location).attr("href", "/search?q[keyword]="+word);
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
   * サーバからのjsonレスポンスをDOMに反映する
   */
  var createContribution = function(data) {
    var contribution = '<div class="contribution"><div class="contribution-user cf"><div class="user--icon">icon</div><div class="user--name">'+data.username+'</div></div><div class="contribution-body"><div class="body--title">'+data.title+'</div><div class="body--content">'+data.content+'</div></div><div class="contribution-footer">';
    if (data.isNew === true) {
      contribution += '<span class="footer--new">New</span>';
    }
    contribution += ' '+data.editedCreatedTime+'</div></div>';
    return contribution;
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