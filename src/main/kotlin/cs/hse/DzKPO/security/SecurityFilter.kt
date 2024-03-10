package cs.hse.DzKPO.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter(
    private val tokenProvider: TokenProvider,
    private val myUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = tokenProvider.getTokenFromHeader(request)
        if (token != null) {
            tokenProvider.validateToken(token)
            val login = tokenProvider.getLoginFromToken(token)
            val userDetails = myUserDetailsService.loadUserByUsername(login)
            val auth =
                UsernamePasswordAuthenticationToken(userDetails.username, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = auth
        }
        filterChain.doFilter(request, response)
    }
}