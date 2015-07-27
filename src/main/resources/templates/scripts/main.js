;(function($) {
  var _document = $(document);

  _document.on('submit', '#post-content', function(event) {
    event.preventDefault();

    var jsondata = {
      content: $('.post-content--text').val()
    };

    $.ajax({
      url: '/post',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(jsondata)
    })
    .done(function() {
      console.log("success");
    })
    .fail(function() {
      console.log("error");
    })
    .always(function() {
      console.log("complete");
    });

  });

}(jQuery));