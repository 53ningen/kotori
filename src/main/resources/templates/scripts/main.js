;(function($) {
  var _document = $(document);

  _document.on('click', '.write-button', function() {
    $('#posts').slideToggle(400);
  })

  _document.on('click', '#submit', function(event) {
    event.preventDefault();

    var jsondata = {
      username: $('.post-content--username').val(),
      title: $('.post-content--title').val(),
      content: $('.post-content--text').val()
    };

    $.ajax({
      url: '/post',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(jsondata)
    })
    .done(function(data) {
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