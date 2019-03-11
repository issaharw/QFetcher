package com.issahar.qfetcher.resources

import com.google.inject.Inject
import com.issahar.qfetcher.dw.QFetcherConfiguration
import com.issahar.qfetcher.fetcher.Fetchers
import com.issahar.qfetcher.model.QuestionResult
import com.issahar.qfetcher.model.QuestionsResult
import com.issahar.qfetcher.model.Sources
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/qfetcher")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class QFetcherResource @Inject constructor(private val configuration: QFetcherConfiguration,
                                           private val fetchers: Fetchers) {

  private val logger = LoggerFactory.getLogger(javaClass)

  @GET
  @Path("/sources")
  fun getSources() = Sources.sources

  @GET
  @Path("/all_questions")
  fun getAllQuestions(): QuestionsResult {
    val start = System.currentTimeMillis()
    // associateWith creates a map between the sources and the result (which is a Future).
    val futures = Sources.sources.associateWith { src ->
      // run all the futures in an async mode, and collect the futures
      CompletableFuture.supplyAsync {
        try {
          // find the fetcher by the source postfix and fetch
          val fetcher = fetchers.fetchers.single { it.shouldFetch(src) }
          fetcher.fetch(src)
        }
        // Problem in source type
        catch (e: IllegalArgumentException) {
          logger.warn("Couldn't find a fetcher for this source: $src")
          emptyList<QuestionResult>()
        }
        // Catch all exceptions from the fetchers, so it won't crash other fetchers.
        catch (e: Exception) {
          logger.error("Problem fetching questions from source.", e)
          emptyList<QuestionResult>()
        }
      }
    }

    // For all the futures, get the result with the timeout.
    // When the timeout happens, log it and return an empty list.
    val allQuestions = futures.map { (src, future) ->
      try {
        future.get(configuration.fetchTimeoutInMillis, TimeUnit.MILLISECONDS)
      }
      catch (e: TimeoutException) {
        logger.warn("Timeout waiting for $src")
        emptyList<QuestionResult>()
      }
    }.flatten() // Each source returns a list, so this is a list of lists. Flatten just, well, flattens it.

    logger.info("It took ${System.currentTimeMillis() - start} milliseconds.")

    return QuestionsResult(allQuestions)
  }
}

