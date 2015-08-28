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
    var $notice = $('.notice--error');

    if (!checkValidate()) {
      $notice.showMsg('入力項目が不完全です');
      return false;
    }

    $this.kotoriAjax({
      url: '/api/register'
    })
    .done(function() {
      // TODO: 新規登録成功時の処理
    })
    .fail(function() {
      var msg = "新規登録に失敗しました";
      $notice.showMsg(msg);
    });
  });

}(jQuery));