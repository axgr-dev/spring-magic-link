package dev.axgr

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class TokenCleaner(private val tokens: TokenRepository) {

  @Scheduled(cron = "0 */1 * * * ?")
  fun clean() {
    val threshold = Instant.now().minus(5, ChronoUnit.MINUTES)
    tokens.deleteExpired(threshold)
  }

}
