package com.issahar.qfetcher.fetcher

import com.google.inject.Inject
import com.google.inject.name.Named
import com.issahar.qfetcher.fetcher.csv.CsvFetcher
import com.issahar.qfetcher.fetcher.image.ImageFetcher
import com.issahar.qfetcher.fetcher.json.JsonFetcher
import javax.ws.rs.client.Client

class Fetchers @Inject constructor(@Named("fetcherClient") private val fetcherClient: Client) {
  val fetchers = setOf(JsonFetcher(fetcherClient),
      CsvFetcher(fetcherClient),
      ImageFetcher(fetcherClient))
}
