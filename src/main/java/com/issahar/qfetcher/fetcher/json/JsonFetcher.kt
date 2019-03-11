package com.issahar.qfetcher.fetcher.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.issahar.qfetcher.fetcher.Fetcher
import com.issahar.qfetcher.model.QuestionResult
import com.issahar.qfetcher.model.SourceType
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType

class JsonFetcher(private val fetcherClient: Client): Fetcher<String> {

  private val mapper = ObjectMapper().registerModule(KotlinModule())

  override val type = SourceType.JSON

  override fun fetch(uri: String): List<QuestionResult> {
    // call the source and get the response as a String, accepting JSON as return type
    val response = fetcherClient.target(uri)
        .request()
        .header("Accept", MediaType.APPLICATION_JSON)
        .get()
    val json = response.readEntity(String::class.java)
    return parse(json)  }

  override fun parse(data: String): List<QuestionResult> {
    val questions = mapper.readValue<JsonSourceQuestions>(data)
    // turning the questions into the expected result objects.
    return questions.questions.map { QuestionResult(it.text, type.desc) }
  }
}

// classes for unmarshalling the json using jackson automatically.
data class JsonSourceQuestions(val questions: List<JsonSourceQuestion>)
data class JsonSourceQuestion(val id: Int,
                              val text: String,
                              val field: String)
