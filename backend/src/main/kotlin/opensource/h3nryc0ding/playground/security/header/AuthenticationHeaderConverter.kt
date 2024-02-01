package opensource.h3nryc0ding.playground.security.header

import opensource.h3nryc0ding.playground.security.TokenProvider
import opensource.h3nryc0ding.playground.security.TokenProvider.BEARER
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationHeaderConverter(
    private val tokenProvider: TokenProvider,
) : ServerAuthenticationConverter {
    override fun convert(serverWebExchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange?.request?.headers?.get(HttpHeaders.AUTHORIZATION)?.firstOrNull())
            .filter { it.length > BEARER.length }
            .map { it.substring(BEARER.length) }
            .filter { it.isNotBlank() }
            .flatMap { authToken ->
                try {
                    Mono.just(tokenProvider.getAuthentication(authToken))
                } catch (_: Exception) {
                    Mono.empty()
                }
            }
    }
}
