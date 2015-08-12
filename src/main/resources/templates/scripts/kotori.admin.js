;(function($) {
  var _document = $(document);
  var updatedata;

  /**
   * 編集ボタンをクリックした時のイベント
   **/
  _document.on('click', '.edit-button', function() {
    var $wrapper = $(this).parent();
    var $updateform = $wrapper.find('#update-contribution');

    if ($updateform.hasClass('active')) {
      $wrapper.css({
        backgroundColor: 'transparent',
        zIndex: 99
      });
      $updateform.css({
        opacity: 0
      }).removeClass('active');
    } else {
      $wrapper.css({
        backgroundColor: '#eee',
        zIndex: 101
      });
      $updateform.addClass('active').css({
        opacity: 1
      });
    }
  });

  /**
   * 更新
   */
  _document.on('submit', '#update-contribution', function(event) {
    event.preventDefault();
    var _this = $(this);
    updatedata = {
      id: _this.find('.update-id').val(),
      username: _this.find('.update-content--username').val(),
      title: _this.find('.update-content--title').val(),
      content: _this.find('.update-content--text').val().replace(/<br>/g, '\\n')
    }
    var jsondata = {
      id: updatedata.id.json_escape(),
      username: updatedata.username.json_escape(),
      title: updatedata.title.json_escape(),
      content: updatedata.content.json_escape()
    }

    $.ajax({
      url: '/api/admin_update',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(jsondata),
    })
    .done(function() {
      console.log("success");
      applyUpdate();
    })
    .fail(function() {
      console.log("error");
      noticeError("投稿の更新");
    });

  });

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

  var applyUpdate = function() {
    $('.contribution-edit').css({
      backgroundColor: 'transparent',
      zIndex: 99
    });
    $('#update-contribution').css({
      opacity: 0
    }).removeClass('active');

    console.log(updatedata);
    $('.user--name').first().text(updatedata.username);
    $('.body--title').first().text(updatedata.title);
    $('.body--content').first().text(updatedata.content);
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