package opensource.h3nryc0ding.playground.security

import opensource.h3nryc0ding.playground.security.TokenProvider.BEARER
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JWTTokenAuthenticationConverter(
    private val tokenProvider: TokenProvider,
) : ServerAuthenticationConverter {
    override fun convert(serverWebExchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange)
            .log()
            .flatMap { exchange ->
                val token = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()
                if (token != null && token.length > BEARER.length) {
                    val authToken = token.substring(BEARER.length)
                    if (authToken.isNotBlank()) {
                        return@flatMap try {
                            Mono.just(tokenProvider.getAuthentication(authToken))
                            // TODO: handle errors better
                        } catch (_: Exception) {
                            Mono.empty()
                        }
                    }
                }
                return@flatMap Mono.empty()
            }
    }
}
