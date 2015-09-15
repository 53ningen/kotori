;(function($) {
  'use strict';

  var $document = $(document);
  var url = $(location).attr('pathname').split('/admin/')[1];

  /**
   * サーバからのjsonレスポンスをDOMに反映する
   */
  var createNGWord = function(data) {
    var ngword = '<div class="ng"><div class="ng__word cf"><div class="ng-id">'+data.id+'</div><div class="ng-word">'+data.word+'</div></div><div class="ng__delete"><form id="delete-ng"><input class="delete-id" type="hidden" name="id" value="'+data.id+'"></form><i class="fa fa-trash-o"></i></div></div>';
    return ngword;
  };

  /**
   * 投稿
   */
  $('#post-ng').on('submit', function(e) {
    e.preventDefault();
    var $this = $(this);

    swal({
      title: 'NGリストに追加しますか？',
      text: $this.find('.input-ng-word').val(),
      allowOutsideClick: true,
      showCancelButton: true,
      closeOnConfirm: false
    }, function() {
      $this.kotoriAjax({
        url: '/api/admin/insert_' + url
      })
      .done(function(data) {
        $this.find('.input-ng-word').val("");
        $('#nglist').prepend($(createNGWord(data)).fadeIn(400));
        $this.showSuccessAlert();
      })
      .fail(function(data) {
        var msg = data.responseText || "追加に失敗しました"
        $this.showErrorAlert(msg);
      });
    });
  });

  /**
   * 削除
   */
  $document.on('click', '.ng__delete', function() {
    var $this = $(this);

    swal({
      title: 'NGリストから削除しますか？',
      text: $this.parent().find('.ng-word').text(),
      allowOutsideClick: true,
      showCancelButton: true,
      closeOnConfirm: false
    }, function() {
      $this.find('form.delete-ng').kotoriAjax({
        url: '/api/admin/delete_' + url
      })
      .done(function() {
        $this.parent().fadeOut(400, function(){
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