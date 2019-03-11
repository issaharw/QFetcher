package com.issahar.qfetcher.fetcher.csv

import com.issahar.qfetcher.fetcher.Fetcher
import com.issahar.qfetcher.model.QuestionResult
import com.issahar.qfetcher.model.SourceType
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType

class CsvFetcher (private val fetcherClient: Client): Fetcher<String> {

  override val type = SourceType.CSV

  override fun fetch(uri: String): List<QuestionResult> {
    val response = fetcherClient.target(uri)
        .request()
        .header("Accept", MediaType.TEXT_PLAIN)
        .get()
    val csv = response.readEntity(String::class.java)
    return parse(csv)
  }

  override fun parse(data: String): List<QuestionResult> {
    val lines = data.lines().drop(1) // get the csv lines without the header line
    // turn each line into a result question object
    return lines.map { line ->
      val fields = line.split(",")
      // the second field is the text
      QuestionResult(fields[1], type.desc)
    }
  }
}
