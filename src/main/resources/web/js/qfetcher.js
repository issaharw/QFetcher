function getSources(table, msg) {
  jQuery.get( "/api/qfetcher/all_questions", function( data ) {
    data["questions"].forEach(function(entry) {
      var src = $("<td></td>").text(entry["source"])
      var text = $("<td></td>").text(entry["value"])
      var row = $("<tr></tr>")
      row.append(src)
      row.append(text)
      $(table).append(row)
    });

    $(msg).remove();
  });
}

