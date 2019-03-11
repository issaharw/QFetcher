package com.issahar.qfetcher

import com.hubspot.dropwizard.guicier.GuiceBundle
import com.issahar.qfetcher.dw.QFetcherConfiguration
import com.issahar.qfetcher.dw.QFetcherHealthCheck
import com.issahar.qfetcher.dw.ServiceModule
import com.issahar.qfetcher.model.Sources
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.slf4j.LoggerFactory
import java.lang.System.exit

/**
 * The main application for DropWizard (the server).
 */
class QFetcherServer : Application<QFetcherConfiguration>() {

  private val logger = LoggerFactory.getLogger(QFetcherServer::class.java)

  override fun getName() = "Q-Fetcher"

  override fun initialize(bootstrap: Bootstrap<QFetcherConfiguration>?) {
    try {
      logger.info("Initializing QFetcher service")

      // register the service module into the server
      val guiceBundle = GuiceBundle.defaultBuilder(QFetcherConfiguration::class.java).modules(ServiceModule()).build()
      bootstrap!!.addBundle(guiceBundle)
      bootstrap.addBundle(AssetsBundle("/web/", "/"))

    } catch (t: Throwable) {
      logger.error("FATAL: error starting up QFetcher service", t)
    }
  }

  override fun run(configuration: QFetcherConfiguration, environment: Environment) {
    try {
      environment.healthChecks().register("FileExistenceHealthCheck", QFetcherHealthCheck())

      // making the resources be under "/api" so the UI could be on the root
      environment.jersey().urlPattern = "/api/*"

      // initialize the sources list here, as it is the load of the server.
      Sources.initFromFile(sourcesFilePath)

    } catch (t: Throwable) {
      logger.error("FATAL: error starting up QFetcher service", t)
      throw t
    }
  }

  companion object {
    var sourcesFilePath: String = ""
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
      if (args.size < 2) {
        println("Usage: java -jar <qfetcer.jar> <config file> <sources file>")
        exit(1)
      }
      val configurationFile = args[0]
      sourcesFilePath = args[1]
      QFetcherServer().run("server", configurationFile)
    }
  }
}
