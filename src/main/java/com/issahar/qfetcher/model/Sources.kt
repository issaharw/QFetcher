package com.issahar.qfetcher.model

import org.slf4j.LoggerFactory
import java.io.File

object Sources {
  val sources = mutableListOf<String>()

  private val logger = LoggerFactory.getLogger(javaClass)

  fun initFromFile(filePath: String) {
    val sourcesFile = File(filePath)
    if (!sourcesFile.isFile || !sourcesFile.exists())
      throw IllegalArgumentException("Sources file doesn't exists: $filePath")

    // get all the sources, and filter them for the ones we support
    val supportedSources = sourcesFile.readLines().filter { src ->
      val type = SourceType.fromSourceURI(src)
      if (type == null )
        logger.warn("Couldn't find a fetcher for this source: $src")

      type != null
    }

    sources.addAll(supportedSources)
  }
}
