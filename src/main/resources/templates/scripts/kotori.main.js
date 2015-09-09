;(function($) {
  'use strict';

  var $document = $(document);

  /**
   * サーバからのjsonレスポンスをDOMに反映する
   */
  var createContribution = function(data) {
    var contribution = '<div class="contribution"><div class="contribution__header"><div class="delete-guide"><form class="delete-contribution"><input class="delete-id" type="hidden" name="id" value="'+data.id+'"></form><i class="fa fa-trash-o"></i></div></div><div class="contribution__user cf"><div class="contribution__user--username">'+data.username+'</div><div class="contribution__user--userid"><small>@'+data.userid+'</small></div></div><div class="contribution__body"><div class="contribution__body--title">'+data.title+'</div><div class="contribution__body--content">'+data.content+'</div></div><div class="contribution__footer"><span class="contribution__footer--new">New</span> '+data.editedCreatedTime+' ・ #'+data.id+'</div></div>';
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
      $this.showSuccessAlert();
      $('#post').slideToggle(400, function() {
        $this.find('input:not(.post-submit), textarea').val("");
      });
      $('#contributions').prepend($(createContribution(data)).fadeIn(400));
    })
    .fail(function(data) {
      var msg = data.responseText || "投稿に失敗しました";
      $this.showErrorAlert(msg);
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
  $document.on('click', '.delete-guide', function() {
    var $this = $(this);

    swal({
      title: '投稿を削除しますか？',
      text: $this.parents('.contribution').find('.contribution__body--content').text(),
      allowOutsideClick: true,
      showCancelButton: true,
      closeOnConfirm: false
    }, function() {
      $this.find('form.delete-contribution').kotoriAjax({
        url: '/api/delete'
      })
      .done(function() {
        $this.parents('.contribution').fadeOut(400, function() {
          $(this).remove();
        });
        $this.showSuccessAlert();
      })
      .fail(function() {
        var msg = "投稿の削除に失敗しました";
        $this.showErrorAlert(msg);
      });
    });
  });

  $document.on('click', '.btn--signout', function() {
    var $this =  $(this);
    swal({
      title: 'ログアウトしますか？',
      allowOutsideClick: true,
      showCancelButton: true,
      closeOnConfirm: false,
    }, function() {
      $this.kotoriAjax({
        url: '/api_logout'
      })
      .done(function() {
        $(location).attr('href', '/login');
      })
      .fail(function() {
        var msg = "ログアウトに失敗しました";
        $this.showErrorAlert(msg);
      });
    });
  });

}(jQuery));