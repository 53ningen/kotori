;(function($) {
  'use strict';

  var $document = $(document);
  $('.update-text').css({ height: $('.contribution__body--content').height() + 'px' });

  /**
   * 投稿の更新を適用する
   */
  var applyUpdate = function($el) {
    var $contribution = $el.parents('.contribution');

    $el.css({
      opacity: 0
    }).removeClass('active');

    var editedData = {};
    $el.serializeArray().forEach(function(el) {
      editedData[el.name] = el.value;
    });

    $contribution.find('.contribution__body--title').first().text(editedData.title);
    $contribution.find('.contribution__body--content').first().text(editedData.content);
  }

  /**
   * 編集ボタンをクリックした時のイベント
   **/
  $document.on('click', '.update-guide', function() {
    var $updateform = $(this).parents('.contribution').find('form.update-contribution');

    if ($updateform.hasClass('active')) {
      $updateform.css({
        opacity: 0
      }).removeClass('active');
    } else {
      $updateform.addClass('active').css({
        opacity: 1
      });
    }
  });

  /**
   * 更新
   */
  $document.on('submit', 'form.update-contribution', function(e) {
    e.preventDefault();
    var $this = $(this);

    swal({
      title: '投稿内容を更新しますか？',
      text: '',
      allowOutsideClick: true,
      showCancelButton: true,
      closeOnConfirm: false
    }, function() {
      $this.kotoriAjax({
        url: '/api/admin/update'
      })
      .done(function() {
        applyUpdate($this);
        $this.showSuccessAlert();
      })
      .fail(function() {
        var msg = "投稿の更新に失敗しました";
        $this.showErrorAlert(msg);
      });
    });

  });

  /**
   * 削除
   */
  $document.on('click', '.delete-guide', function() {
    var $this = $(this);

    swal({
      title: '投稿を削除しますか？',
      text: $this.parents('.contribution').find('.contribution__body--content').first().text(),
      allowOutsideClick: true,
      showCancelButton: true,
      closeOnConfirm: false
    }, function() {
      $this.find('form.delete-contribution').kotoriAjax({
        url: '/api/admin/delete'
      })
      .done(function() {
        $this.parents('.contribution').fadeOut(400, function(){
          $(this).remove();
        });
        $this.showSuccessAlert();
      })
      .fail(function() {
        var msg = "削除に失敗しました。既に削除済みの可能性があります";
        $this.showErrorAlert(msg);
      });
    });
  });

}(jQuery));