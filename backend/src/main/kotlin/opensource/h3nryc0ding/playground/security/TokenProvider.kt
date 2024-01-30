package opensource.h3nryc0ding.playground.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.Date

@Component
object TokenProvider {
    private val log = LoggerFactory.getLogger(this::class.java)

    private const val TOKEN_VALIDITY = 5 * 60 * 60 * 1000L
    private const val AUTHORITIES_KEY = "auth"
    private const val ISSUER = "h3nryc0ding"
    const val BEARER = "Bearer "

    private val KEY = Jwts.SIG.HS256.key().build()

    fun createToken(authentication: Authentication): String {
        val authorities =
            authentication.authorities
                .joinToString(",") { it.authority }

        val now = Date().time
        val validity = Date(now + TOKEN_VALIDITY)

        log.info("Creating token for user ${authentication.name}")

        return Jwts.builder()
            .issuer(ISSUER)
            .subject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .expiration(validity)
            .signWith(KEY)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        if (token.isBlank() || !validateToken(token)) {
            throw BadCredentialsException("Invalid token")
        }
        val claims =
            Jwts.parser()
                .requireIssuer(ISSUER)
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .payload

        val authorities =
            claims[AUTHORITIES_KEY]
                .toString()
                .split(",")
                .filter { it.isNotEmpty() }
                .map { SimpleGrantedAuthority(it) }

        val principal = User(claims.subject, "", authorities)
        log.info("Creating authentication for user ${principal.username}")
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    private fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser()
                .requireIssuer(ISSUER)
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(authToken)

            return true
        } catch (e: SignatureException) {
            log.info("Invalid JWT signature.")
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT token.")
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT token.")
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT token.")
        } catch (e: IllegalArgumentException) {
            log.info("JWT token compact of handler are invalid.")
        }
        log.info("Invalid token")
        return false
    }
}
