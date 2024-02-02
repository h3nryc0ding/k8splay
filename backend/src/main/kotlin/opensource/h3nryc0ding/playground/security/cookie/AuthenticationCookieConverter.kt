package opensource.h3nryc0ding.playground.security.cookie

import opensource.h3nryc0ding.playground.security.TokenProvider
import opensource.h3nryc0ding.playground.security.TokenProvider.COOKIE
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationCookieConverter(
    private val tokenProvider: TokenProvider,
) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(exchange?.request?.cookies?.get(COOKIE)?.firstOrNull()?.value)
            .checkpoint(".request.cookies.get(COOKIE).firstOrNull()?.value")
            .filter { it.isNotEmpty() }
            .checkpoint(".filter { it.isNotEmpty() }")
            .flatMap { authToken ->
                try {
                    Mono.just(tokenProvider.getAuthentication(authToken))
                } catch (_: Exception) {
                    Mono.empty()
                }
            }
    }
}
