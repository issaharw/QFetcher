package com.issahar.qfetcher.fetcher

import com.issahar.qfetcher.model.QuestionResult
import com.issahar.qfetcher.model.SourceType

interface Fetcher<T> {
  // properties could also be overridden
  val type: SourceType
  // default implementation inside an interface
  fun shouldFetch(uri: String): Boolean = SourceType.fromSourceURI(uri) == type
  fun fetch(uri: String): List<QuestionResult>
  fun parse(data: T): List<QuestionResult>
}
