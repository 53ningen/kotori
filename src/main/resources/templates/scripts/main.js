;(function($) {
  var _document = $(document);

  _document.on('submit', '#post-content', function(event) {
    event.preventDefault();

    var jsondata = {
      title: $('.post-content--title').val(),
      content: $('.post-content--text').val()
    };

    console.log(JSON.stringify(jsondata));

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