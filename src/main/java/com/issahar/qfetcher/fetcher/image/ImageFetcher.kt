package com.issahar.qfetcher.fetcher.image

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.issahar.qfetcher.fetcher.Fetcher
import com.issahar.qfetcher.model.QuestionResult
import com.issahar.qfetcher.model.SourceType
import org.apache.commons.codec.binary.Base64
import javax.ws.rs.client.Client
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType

private const val GOOGLE_OCR_URL = "https://vision.googleapis.com/v1/images:annotate"
private const val GOOGLE_OCR_API_KEY = "AIzaSyC2xvtuShYcIRE_bMoeTOB1kg0H5U9gUlU"

class ImageFetcher (private val fetcherClient: Client): Fetcher<ByteArray> {

  override val type = SourceType.IMAGE
  private val mapper = ObjectMapper().registerModule(KotlinModule())

  override fun fetch(uri: String): List<QuestionResult> {
    val response = fetcherClient.target(uri)
        .request()
        .get()
    val img = response.readEntity(ByteArray::class.java)
    return parse(img)
  }

  override fun parse(data: ByteArray): List<QuestionResult> {
    val googleRequest = getGoogleRequest(data)
    val response = fetcherClient.target(GOOGLE_OCR_URL)
        .queryParam("key", GOOGLE_OCR_API_KEY)
        .request(MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"))
        .header("Accept", MediaType.APPLICATION_JSON)
        .post(Entity.entity(googleRequest, MediaType.APPLICATION_JSON_TYPE))

    val responseJson = response.readEntity(String::class.java)
    val text = getQuestionTextFromGoogleResponse(responseJson)
    return listOf(QuestionResult(text, type.desc))
  }


  private fun getGoogleRequest(data: ByteArray): GoogleApiRequests {
    val imageBase64 = Base64.encodeBase64String(data)
    return GoogleApiRequests(listOf(GoogleApiRequest(GoogleApiImage(imageBase64), listOf(GoogleApiImageFeature("TEXT_DETECTION")))))
  }

  private fun getQuestionTextFromGoogleResponse(json: String): String {
    val questions = mapper.readValue<Map<String, Any>>(json)
    return (((questions["responses"] as List<Any>)[0] as Map<String, Any>)["fullTextAnnotation"] as Map<String, Any>)["text"] as String
  }

}

// Data classes for automatic marshaling of the google JSON needed for the OCR request
data class GoogleApiRequests(val requests: List<GoogleApiRequest>)

data class GoogleApiRequest(val image: GoogleApiImage, val features: List<GoogleApiImageFeature>)

data class GoogleApiImage(val content: String)

data class GoogleApiImageFeature(val type: String)
