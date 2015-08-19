;(function($) {
  'use strict';

  $.fn.kotoriAjax = function(options) {
    var $this = this;
    var json = createJsonData($this);
    var defaults = {type: 'post'};
    var settings = $.extend(defaults, options);
    return connectByAjax(settings, json);
  }

  $.fn.showMsg = function(msg) {
    var $this = this;
    $this.find('.notice__text').text(msg);
    $this.addClass('active').css({
        top: 0,
        opacity: 1
      });
    setTimeout(function() {
      $this.css({
        top: '-70px',
        opacity: 0
      }).removeClass('active');
    }, 3000);
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
  }

  /**
   * 受け取ったFormからjson文字列を返す
   */
  var createJsonData = function($formData) {
    var json = {};
    var formArray = $formData.serializeArray();

    formArray.forEach(function(el) {
      json[el.name] = el.value.json_escape();
    });

    return JSON.stringify(json);
  };

  /**
   * Ajax通信を行う
   */
  var connectByAjax = function(options, json) {
    var defer = $.Deferred();

    $.ajax({
      url: options.url,
      type: options.type,
      contentType: 'application/json',
      data: json,
    })
    .done(function(data) {
      defer.resolve(data);
    })
    .fail(function(data) {
      defer.reject(data);
    });

    return defer.promise();
  };

}(jQuery));