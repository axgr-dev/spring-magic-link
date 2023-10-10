package dev.axgr

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.time.Instant

@Service
class MagicService(private val users: UserDetailsService, private val tokens: TokenRepository) {

  private val random = SecureRandom()
  private val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

  private val strategy = SecurityContextHolder.getContextHolderStrategy()
  private val repo = HttpSessionSecurityContextRepository()

  companion object {
    private val log = LoggerFactory.getLogger(MagicService::class.java)
  }

  @Transactional
  fun authenticate(token: String, request: HttpServletRequest, response: HttpServletResponse) {
    val entity = tokens.findByToken(token)

    entity?.let {
      val user = users.loadUserByUsername(it.username)
      val authentication = UsernamePasswordAuthenticationToken(user, user.password, user.authorities)
      val context = strategy.createEmptyContext()
      context.authentication = authentication
      strategy.context = context
      repo.saveContext(context, request, response)
      tokens.deleteById(entity.id!!)
    }
  }

  fun issueToken(username: String) {
    val user = users.loadUserByUsername(username)

    val token = token()
    val entity = tokens.save(MagicToken().apply {
      this.username = user.username
      this.token = token
      this.created = Instant.now()
    })

    mail(entity)
  }

  private fun token(size: Int = 64): String {
    return (1..size)
      .map { alphabet[random.nextInt(alphabet.length)] }
      .joinToString("")
  }

  /**
   * Send an email to the user with the magic link.
   */
  private fun mail(token: MagicToken) {
    log.info("${token.username} has magic link http://localhost:8080/auth/${token.token}")
  }

}
