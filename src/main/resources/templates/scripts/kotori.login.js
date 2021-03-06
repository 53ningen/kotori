;(function($) {
  'use strict';

  /**
   * Initialize
   */
  var old = {
    userid: "",
    username: ""
  };

  var validate = {
    userid: false,
    username: false,
    password: false
  };

  /**
   * inputの変更があるか確認する
   */
  var checkChange = function(el, index) {
    var val = $(el).val();
    if (old[index] !== val) {
      old[index] = val;
      return true;
    } else {
      return false;
    }
  }

  /**
   * 不正なパラメータが含まれていないか確認する
   */
  var checkValidate = function() {
    var isValidate = true;
    Object.keys(validate).forEach(function(key) {
      if (!validate[key]) {
        isValidate = false;
      }
    });
    return isValidate;
  }

  /**
   * 不正な文字列が入っていないか確認する
   */
  var validateText = function(str, pattern) {
    if (str.match(pattern)) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * エラーメッセージを表示する
   */
  var showTip = function($el, index) {
    $el.css({ opacity: 1 });
    validate[index] = false;
  }

  /**
   * エラーメッセージを非表示にする
   */
  var hiddenTip = function($el, index) {
    $el.css({ opacity: 0 });
    validate[index] = true;
  }

  /**
   * ユーザID欄の変更監視
   */
  $('.register-userid').on('keyup', function() {
    var index = 'userid';
    var isChange = checkChange(this, index);
    if (isChange) {
      var val = $(this).val();
      var pattern = /\W/g;
      var tip = $('.tip-userid');
      if (validateText(val, pattern)) {
        hiddenTip(tip, index);
      } else {
        showTip(tip, index);
      }
    }
  });

  /**
   * ユーザ名欄の変更監視
   */
  $('.register-username').on('keyup', function() {
    var index = 'username';
    var isChange = checkChange(this, index);
    if (isChange) {
      var val = $(this).val();
      var tip = $('.tip-username');
      var pattern = /^\s+$/;
      if (validateText(val, pattern)) {
        hiddenTip(tip, index);
      } else {
        showTip(tip, index);
      }
    }
  });

  /**
   * パスワード欄の変更監視
   */
  $('.register-password').on('keyup', function() {
    var index = 'password';
    var val = $(this).val();
    var tip = $('.tip-password');
    var pattern = /^.{1,7}$/;
    if (validateText(val, pattern)) {
      hiddenTip(tip, index);
    } else {
      showTip(tip, index);
    }
  });

  /**
   * 新規登録
   */
  $('form#register-form').on('submit', function(e) {
    e.preventDefault();
    var $this = $(this);

    if (!checkValidate()) {
      var msg = "入力項目が不完全です"
      $this.showErrorAlert(msg);
      return false;
    }

    swal({
      title: '以下の内容で登録します',
      text: 'ユーザID: ' + $this.find('.register-userid').val() + '<br>' +
            'ユーザ名: ' + $this.find('.register-username').val() + '<br>' +
            'パスワード: ' + Array($this.find('.register-password').val().length).join('●'),
      html: true,
      allowOutsideClick: true,
      showCancelButton: true,
      closeOnConfirm: false,
    }, function() {
      $this.kotoriAjax({
        url: '/api_register'
      })
      .done(function() {
        $this.find('input:not(.register-submit)').val("");
        $this.showSuccessAlert();
        setTimeout(function() {
          $(location).attr('href', '/');
        }, 1000);
      })
      .fail(function(data) {
        var msg = data.responseText || "新規登録に失敗しました";
        $this.showErrorAlert(msg);
      });
    });

  });

  /**
   * ログイン
   */
  $('form#login-form').on('submit', function(e) {
    e.preventDefault();
    var $this = $(this);

    $this.kotoriAjax({
      url: '/api_login'
    })
    .done(function() {
      $(location).attr('href', '/');
    })
    .fail(function(data) {
      var msg = data.responseText || "ユーザIDまたはパスワードが違います";
      $this.showErrorAlert(msg);
    })
  })

  /**
   * ログインモーダル
   */
  $('.btn--signin').on('click', function() {
    var $login = $('#login');

    if ($login.hasClass('active')) {
      $login.css({ opacity: 0 }).removeClass('active');
    } else {
      $login.addClass('active').css({ opacity: 1 });
    }
  });

}(jQuery));