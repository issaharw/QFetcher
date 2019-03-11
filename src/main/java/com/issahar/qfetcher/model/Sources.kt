package com.issahar.qfetcher.model

import java.io.File

object Sources {
  val sources = mutableListOf<String>()

  fun initFromFile(filePath: String) {
    val sourcesFile = File(filePath)
    if (!sourcesFile.isFile || !sourcesFile.exists())
      throw IllegalArgumentException("Sources file doesn't exists: $filePath")

    sources.addAll(sourcesFile.readLines())
  }
}
