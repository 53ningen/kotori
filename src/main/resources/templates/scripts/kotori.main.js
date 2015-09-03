;(function($) {
  'use strict';

  var $document = $(document);
  var $notice = $('.notice--error');

  /**
   * サーバからのjsonレスポンスをDOMに反映する
   */
  var createContribution = function(data) {
    var contribution = '<div class="contribution"><div class="contribution__user cf"><div class="contribution__user--icon">icon</div><div class="contribution__user--name">'+data.username+'</div></div><div class="contribution__body"><div class="contribution__body--title">'+data.title+'</div><div class="contribution__body--content">'+data.content+'</div></div><div class="contribution__footer">';
    if (data.isNew === true) {
      contribution += '<span class="contribution__footer--new">New</span>';
    }
    contribution += ' '+data.editedCreatedTime+' ・ '+data.id+'</div></div>';
    return contribution;
  };

  /**
   * 投稿
   */
  $('#post-contribution').on('submit', function(e) {
    e.preventDefault();
    var $this = $(this);
    $this.kotoriAjax({
      url: '/api/post'
    })
    .done(function(data) {
      $('#post').slideToggle(400, function() {
        $this.find('input:not(.post-submit), textarea').val("");
      });
      $('#contributions').prepend($(createContribution(data)).fadeIn(400));
    })
    .fail(function(data) {
      var msg = data.responseText || "新規投稿に失敗しました";
      $notice.showMsg(msg);
    });
  });

  /**
   * 検索
   */
  $('#search-contribution').on('submit', function(e) {
    e.preventDefault();
    var $this = $(this);
    var keyword = $this.find('.search-keyword').val();
    $(location).attr('href', '/search?q[keyword]=' + keyword);
  });

  /**
   * 削除
   */
  $document.on('submit', '#delete-contribution', function(e) {
    e.preventDefault();
    var $this = $(this);
    $this.kotoriAjax({
      url: '/api/delete'
    })
    .done(function() {
      $this.parents('.contribution').fadeOut(400, function() {
        $(this).remove()
      });
    })
    .fail(function() {
      var msg = "投稿の削除に失敗しました";
      $notice.showMsg(msg);
      $this.find('.delete-key').val("");
    });
  });

}(jQuery));