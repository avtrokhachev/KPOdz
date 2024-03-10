package cs.hse.DzKPO.security

import com.fasterxml.jackson.databind.ObjectMapper
import cs.hse.DzKPO.features.users.db.entities.Role
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.time.LocalDateTime

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val securityFilter: SecurityFilter,
    private val exceptionHandlerFilter: ExceptionHandlerFilter
) {
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .csrf()
            .disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            .anyRequest().permitAll()
        http
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(exceptionHandlerFilter, SecurityFilter::class.java)
            .exceptionHandling { configurer ->
                configurer.authenticationEntryPoint { request, response, authException ->
                    val jsonResponse = mapOf(
                        "message" to "Access denied",
                    )
                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write(
                        ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonResponse)
                    )
                }
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}