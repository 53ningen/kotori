;(function($) {
  'use strict';

  var $document = $(document);
  var $notice = $('.notice--error');
  var url = $(location).attr('pathname').split('_')[1];

  /**
   * サーバからのjsonレスポンスをDOMに反映する
   */
  var createNGWord = function(data) {
    var ngword = '<div class="ng"><div class="ng__word"><span class="ng-word">'+data.word+'</span></div></div>';
    return ngword;
  };

  /**
   * 投稿
   */
  $('#post-ng').on('submit', function(e) {
    e.preventDefault();
    var $this = $(this);
    $this.kotoriAjax({
      url: '/api/admin_insert_' + url
    })
    .done(function(data) {
      $this.find('.ng-word').val("");
      $('#nglist').prepend($(createNGWord(data)).fadeIn(400));
    })
    .fail(function(data) {
      var msg = data.responseText || "追加に失敗しました"
      $notice.showMsg(msg);
    });

  });

  /**
   * 削除
   */
  $document.on('click', '.ng__delete', function() {
    var $this = $(this);
    $('#delete-ng').kotoriAjax({
      url: '/api/admin_delete_' + url
    })
    .done(function() {
      $this.parent().fadeOut(400, function(){
        $(this).remove();
      });
    })
    .fail(function() {
      var msg = "削除に失敗しました";
      $notice.showMsg(msg);
    });
  });

}(jQuery));