;(function($) {
  var _document = $(document);
  var $posts = $('#posts');
  var $search = $('#search');

  /**
   * 新規投稿ボタンをクリックした時のイベント
   */
  _document.on('click', '.write-button', function() {
    toggleHeader($posts, $search);
    /*
    var $details = $('.write-button-details');

    if ($details.hasClass('active')) {
      $details.animate({
        top: 0,
        opacity: 0 }, 400, function() {
          $details.removeClass('active');
        });
    } else {
      $details.addClass('active').animate({
        top: '40px',
        opacity: 1 }, 400);
    }
    */
  });

  _document.on('click', '.hc--search', function() {
    toggleHeader($search, $posts);
  });

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

  _document.on('click', '.close-button', function() {
    var $settings = $('.settings');
    $settings.removeClass('active').css({
      top: '-70px',
      opacity: 0
    });
  })

  _document.on('mouseenter', '.main-circle', function() {
    var $buttonMenu = $(this).find('.button-menu');
    $buttonMenu.css({opacity: 1});
  });
  _document.on('mouseleave', '.main-circle', function() {
    var $buttonMenu = $(this).find('.button-menu');
    $buttonMenu.css({opacity: 0});
  });

  /**
   * 投稿ボタンをクリックした時のイベント
   */
  _document.on('click', '#submit', function(event) {
    event.preventDefault();

    var jsondata = {
      username: $('.post-content--username').val().json_escape(),
      title: $('.post-content--title').val().json_escape(),
      content: $('.post-content--text').val().json_escape()
    };

    $.ajax({
      url: '/post',
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
      });
      $('#contributions').prepend($(createContribution(data)).fadeIn(400));
    })
    .fail(function() {
      console.log("error");
    })
    .always(function() {
      console.log("complete");
    });

  });

  _document.on('submit', '#search-contribution', function(event) {
    event.preventDefault();

    var word = $('.search-content--word').val();
    $(location).attr("href", "/search?q[title]="+word);
  })

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
   * サーバからのjsonレスポンスをDOMに反映する
   */
  var createContribution = function(data) {
    var contribution = '<div class="contribution"><div class="contribution-user cf"><div class="user--icon">icon</div><div class="user--name">'+data.username+'</div></div><div class="contribution-body"><div class="body--title">'+data.title+'</div><div class="body--content">'+data.content+'</div></div><div class="contribution-footer">';
    if (data.isNew === true) {
      contribution += '<span class="footer--new">New</span>';
    }
    contribution += ' '+data.editedCreatedTime+' ・ #'+data.id+'</div></div>';
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
  };

}(jQuery));