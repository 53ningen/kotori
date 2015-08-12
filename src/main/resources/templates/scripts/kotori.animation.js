;(function($) {
  var _document = $(document);
  var $posts = $('#posts');
  var $search = $('#search');

  /**
   * 新規投稿ボタンをクリックした時のイベント
   */
  _document.on('click', '.write-button', function() {
    toggleHeader($posts, $search);
  });

  /**
   * 検索ボタンをクリックした時のイベント
   */
  _document.on('click', '.hc--search', function() {
    toggleHeader($search, $posts);
  });

  /**
   * 設定ボタンをクリックした時のイベント
   */
  _document.on('click', '.hc--setting', function() {
    var $details = $('.user-button-details');

    if ($details.hasClass('active')) {
      $details.removeClass('active').css({
        top: 0,
        opacity: 0
      });
    } else {
      $details.addClass('active').css({
        top: '60px',
        opacity: 1
      });
    }
  });
  _document.on('click', '.main-circle', function() {
    var $details = $('.user-button-details');
    var $settings = $('.settings');

    if ($settings.hasClass('active')) {
      $settings.removeClass('active').css({
        top: '-70px',
        opacity: 0
      });
    } else {
      if (!$details.hasClass('active')) return false;
      $settings.addClass('active').css({
        top: 0,
        opacity: 1
      });
    }
  });

  /**
   * 閉じるボタンをクリックした時のイベント
   */
  _document.on('click', '.close-button', function() {
    var $settings = $('.settings');
    $settings.removeClass('active').css({
      top: '-70px',
      opacity: 0
    });
  })

  /**
   * マウスホバー時のテキスト表示
   */
  _document.on('mouseenter', '.main-circle', function() {
    var $buttonMenu = $(this).find('.button-menu');
    $buttonMenu.css({opacity: 1});
  });
  _document.on('mouseleave', '.main-circle', function() {
    var $buttonMenu = $(this).find('.button-menu');
    $buttonMenu.css({opacity: 0});
  });

  /**
   * 削除ボタンをクリックした時のイベント
   */
  _document.on('click', '.del-guide', function() {
    var wrap = $(this).parent().find('.del-wrap').get(0);

    $(wrap).slideToggle(400);
  });

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

}(jQuery));