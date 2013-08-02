$(window).load(function() {
  $.ajax({
    url: './README.md', type: 'get', dataType: 'text',
    beforeSend: function() {}, complete: function() {}, 
    success: function(htmlData) {
      var converter = new Markdown.Converter();
      var mdText = converter.makeHtml(htmlData);
      $("#readme").append(mdText);
    }
  });

  $.getJSON('./jgl-demos.json', function(data) {
    var items = [];
    $.each(data, function(index, element) {
      var item = sprintf('<li><a href="./jnlp/%s.jnlp">%s</a></li>', element.id, element.id);
      items.push(item);
    });
    var demoList = $('<ul/>', { html: items.join('') });
    $("#webstart").append(demoList);
  });
});