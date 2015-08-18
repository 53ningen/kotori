;(function($) {
  'use strict';

  var $post = $('#post');
  var $search = $('#search');

  /**
   * ヘッダーメニューの開閉を行う
   */
  var toggleHeader = function($current, $target) {
    if ($current.hasClass('active')) {
      slOff($current);
    } else {
      if ($target.hasClass('active')) {
        slOff($target);
      }
      slOn($current);
    }

    function slOn($cur) {
      $cur.addClass('active').slideDown(400);
    }
    function slOff($cur) {
      $cur.removeClass('active').slideUp(400);
    }
  }

  /**
   * 新規投稿ボタンをクリックした時のイベント
   */
  $('.btn--write').on('click', function() {
    toggleHeader($post, $search);
  });

  /**
   * 検索ボタンをクリックした時のイベント
   */
  $('.btn--search').on('click', function() {
    toggleHeader($search, $post);
  });

  /**
   * 削除ボタンをクリックした時のイベント
   */
  $('.delete-guide').on('click', function() {
    var wrap = $(this).parent().find('.delete-wrapper').get(0);
    $(wrap).slideToggle(400);
  });

}(jQuery));