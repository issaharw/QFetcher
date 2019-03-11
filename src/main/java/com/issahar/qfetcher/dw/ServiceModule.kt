package com.issahar.qfetcher.dw

import com.google.inject.Binder
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.hubspot.dropwizard.guicier.DropwizardAwareModule
import com.issahar.qfetcher.fetcher.Fetchers
import com.issahar.qfetcher.resources.QFetcherResource
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.setup.Environment
import org.glassfish.jersey.client.ClientProperties
import org.glassfish.jersey.logging.LoggingFeature
import javax.ws.rs.client.Client

/**
 * Injection configuration
 */
class ServiceModule : DropwizardAwareModule<QFetcherConfiguration>() {

  override fun configure(binder: Binder) {
    binder.bind(QFetcherResource::class.java)
    binder.bind(Fetchers::class.java)
  }

  @Provides
  @Singleton
  @Named("fetcherClient")
  private fun getFetcherClient(environment: Environment, configuration: QFetcherConfiguration): Client {
    return JerseyClientBuilder(environment).using(configuration.jerseyClientConfig)
        // Use the configured timeout as the read and socket timeout.
        .withProperty(ClientProperties.CONNECT_TIMEOUT, configuration.fetchTimeoutInMillis.toInt())
        .withProperty(ClientProperties.READ_TIMEOUT, configuration.fetchTimeoutInMillis.toInt())
        .withProperty(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.HEADERS_ONLY)
        .withProperty(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO")
        .build("fetcherClient")
  }

}
