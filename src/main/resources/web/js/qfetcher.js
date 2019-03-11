function getSources(sources) {
  $.get("/api/qfetcher/sources")
  .done(function( data ) {
    data.forEach(function(entry) {
      var option = $('<option value="' + entry + '"></option>').text(entry);
      $(sources).append(option);
    });
  });
}

function getFilteredQuestions(sources, div  ) {
  var selectedSources = getSelectedSources(sources)
  $.ajax("/api/qfetcher/all_questions", {
    data : JSON.stringify(selectedSources),
    contentType : 'application/json',
    type : 'POST'})
   .done(function( data ) {
      showQuestions(data, div);
  });
}

function getSelectedSources(sourcesElem) {
  var sources = [];
  $.each($(sourcesElem + " option:selected"), function(){
      sources.push($(this).val());
  });
  return sources
}

function showQuestions(data, div) {
  var table = $('<table id="questions">' +
          '<tr>' +
              '<th id="sourceHeader">Source</th>' +
              '<th>Question Text</th>' +
          '</tr>' +
      '</table>')
  $(div).empty()
  $(div).append(table)
  data["questions"].forEach(function(entry) {
    var src = $("<td></td>").text(entry["source"])
    var text = $("<td></td>").text(entry["value"])
    var row = $("<tr></tr>")
    row.append(src)
    row.append(text)
    $(table).append(row)
  });
}

