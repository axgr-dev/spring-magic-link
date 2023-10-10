package dev.axgr

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class AuthController(private val service: MagicService) {

  companion object {
    private val log = LoggerFactory.getLogger(AuthController::class.java)
  }

  @GetMapping("/me")
  fun me(principal: Principal): String {
    return "Hello, ${principal.name}!"
  }

  @Async
  @PostMapping("/auth")
  fun login(@RequestBody request: AuthRequest) {
    try {
      service.issueToken(request.username)
    } catch (cause: Exception) {
      log.error("Failed to issue token for ${request.username}")
    }
  }

  @GetMapping("/auth/{token}")
  fun authenticate(@PathVariable token: String, request: HttpServletRequest, response: HttpServletResponse){
    service.authenticate(token, request, response)
  }
}

data class AuthRequest(val username: String)
