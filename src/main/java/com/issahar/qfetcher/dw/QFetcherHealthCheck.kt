package com.issahar.qfetcher.dw

import com.codahale.metrics.health.HealthCheck

/**
 * Basic health check, as part of dropwizard, so it won't log warnings
 */
class QFetcherHealthCheck : HealthCheck() {
   override fun check(): Result {
      return Result.healthy()
    }
 }
