package opensource.h3nryc0ding.playground.security

import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class TokenAuthenticationConverter(
    private val tokenProvider: TokenProvider,
) : ServerAuthenticationConverter {
    companion object {
        private const val BEARER = "Bearer "
    }

    override fun convert(serverWebExchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange)
            .log()
            .flatMap { exchange ->
                val token = getTokenFromRequest(exchange)
                if (token != null && token.length > BEARER.length) {
                    val authToken = token.substring(BEARER.length)
                    if (authToken.isNotBlank()) {
                        return@flatMap Mono.just(tokenProvider.getAuthentication(authToken))
                    }
                }
                return@flatMap Mono.empty()
            }
    }

    private fun getTokenFromRequest(serverWebExchange: ServerWebExchange): String? {
        return serverWebExchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
    }
}
