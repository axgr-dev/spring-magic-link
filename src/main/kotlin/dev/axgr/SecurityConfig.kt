package dev.axgr

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import javax.sql.DataSource

@Configuration
class SecurityConfig {

  @Bean
  fun users(datasource: DataSource): UserDetailsService = JdbcUserDetailsManager(datasource)

  @Bean
  fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

  @Bean
  fun chain(http: HttpSecurity): SecurityFilterChain {
    http
      .authorizeHttpRequests { requests ->
        requests
          .requestMatchers(HttpMethod.POST, "/auth").permitAll()
          .requestMatchers(HttpMethod.GET, "/auth/*").permitAll()
          .anyRequest().authenticated()
      }
      .csrf { csrf -> csrf.disable() }

    return http.build()
  }

}
