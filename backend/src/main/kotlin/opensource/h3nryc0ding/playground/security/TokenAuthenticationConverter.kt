package opensource.h3nryc0ding.playground.security

import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.function.Function

@Component
class TokenAuthenticationConverter(
    private val tokenProvider: TokenProvider,
) : Function<ServerWebExchange, Mono<Authentication>> {
    companion object {
        private const val BEARER = "Bearer "
    }

    override fun apply(serverWebExchange: ServerWebExchange): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange)
            .mapNotNull {
                getTokenFromRequest(it)?.takeIf { token ->
                    token.length > BEARER.length
                }
            }
            .mapNotNull {
                it?.substring(BEARER.length)
            }
            .filter {
                !it.isNullOrBlank()
            }
            .mapNotNull {
                // TODO: check cast is safe
                tokenProvider.getAuthentication(it!!)
            }
    }

    private fun getTokenFromRequest(serverWebExchange: ServerWebExchange): String? {
        return serverWebExchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
    }
}
