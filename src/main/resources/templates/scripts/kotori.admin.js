;(function($) {
  'use strict';

  var $document = $(document);
  var $notice = $('.notice--error');

  /**
   * 投稿の更新を適用する
   */
  var applyUpdate = function(el) {
    var $contribution = el.parents('.contribution');

    $contribution.find('.contribution__edit').css({
      backgroundColor: 'transparent',
      zIndex: 99
    });
    el.css({
      opacity: 0
    }).removeClass('active');

    var editedData = {};
    el.serializeArray().forEach(function(el) {
      editedData[el.name] = el.value;
    });

    $contribution.find('.contribution__user--name').first().text(editedData.username);
    $contribution.find('.contribution__body--title').first().text(editedData.title);
    $contribution.find('.contribution__body--content').first().text(editedData.content);
  }

  /**
   * 編集ボタンをクリックした時のイベント
   **/
  $document.on('click', '.edit-guide', function() {
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
  $document.on('submit', '#update-contribution', function(e) {
    e.preventDefault();
    var $this = $(this);
    $this.kotoriAjax({
      url: '/api/admin_update'
    })
    .done(function() {
      applyUpdate($this);
    })
    .fail(function() {
      var msg = "投稿の更新に失敗しました";
      $notice.showMsg(msg);
    });
  });

  /**
   * 削除
   */
  $document.on('submit', '#delete-contribution', function(e) {
    e.preventDefault();
    var $this = $(this);
    $this.kotoriAjax({
      url: '/api/admin_delete'
    })
    .done(function() {
      $this.parents('.contribution').fadeOut(400, function(){
        $(this).remove();
      });
    })
    .fail(function() {
      var msg ="投稿の削除に失敗しました";
      $notice.showMsg(msg);
    });
  });

}(jQuery));