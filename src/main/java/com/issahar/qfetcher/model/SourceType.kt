package com.issahar.qfetcher.model

import java.lang.IllegalArgumentException

enum class SourceType(val desc: String) {
  JSON("json"),
  CSV("csv"),
  IMAGE("image");

  companion object {

    fun fromSourceURI(uri: String): SourceType? {
      val postfix = uri.toLowerCase().substringAfterLast(".")
      return when (postfix) {
        "json" -> JSON
        "csv" -> CSV
        "png", "jpg", "bmp" -> IMAGE
        else -> null
      }
    }
  }
}
