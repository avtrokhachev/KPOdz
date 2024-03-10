package cs.hse.DzKPO.security

import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class TokenProvider {
    private val secret: String = "some_secret"

    fun validateToken(token: String): Boolean {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
        return true
    }

    private fun getAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    fun getTokenFromHeader(request: HttpServletRequest): String? {
        val authToken = request.getHeader("Authorization")
        if (StringUtils.hasText(authToken) && authToken.startsWith("Bearer ")) {
            return authToken.substring(7, authToken.length)
        }
        return null
    }

    fun getClaimsFromToken(token: String): Claims? {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token)?.body
    }

    fun getLoginFromToken(token: String): String {
        val claims = getAllClaims(token)
        return claims.subject
    }

    fun generateToken(claims: Claims): String {
        return Jwts.builder().setClaims(claims).setIssuer("SERVICE_ACCOUNT").signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    fun generateTokenForUser(login: String, roles: List<String>): String {
        val claims = Jwts.claims().setSubject(login).apply { this["role"] = roles }
        return generateToken(claims)
    }
}