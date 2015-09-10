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
   * 投稿数変更ボタンをクリックした時のイベント
   */
  $('.btn--limit').on('click', function() {
    $('.guide__btn').each(function(index) {
      var $this = $(this);
      if ($this.hasClass('active')) {
        $this.animate({
          opacity: 0,
          left: 0
        }, 300).removeClass('active');
      } else {
        $this.addClass('active').animate({
          opacity: 1,
          left: (index + 1) * 45 + 'px'
        }, 300)
      }
    });
  });

}(jQuery));