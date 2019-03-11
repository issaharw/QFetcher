package com.issahar.qfetcher.dw

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.client.JerseyClientConfiguration

/**
 * Configuration class that can be injected throughout the code
 */
class QFetcherConfiguration : Configuration() {
  @JsonProperty("jerseyClient") var jerseyClientConfig = JerseyClientConfiguration()
  val fetchTimeoutInMillis: Long = 30_000
}
