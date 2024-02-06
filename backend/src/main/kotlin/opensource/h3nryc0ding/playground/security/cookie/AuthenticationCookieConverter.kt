package opensource.h3nryc0ding.playground.security.cookie

import opensource.h3nryc0ding.playground.security.TokenProvider
import opensource.h3nryc0ding.playground.security.TokenProvider.COOKIE
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class AuthenticationCookieConverter(
    private val tokenProvider: TokenProvider,
) : ServerAuthenticationConverter {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(exchange?.request?.cookies?.get(COOKIE)?.firstOrNull()?.value)
            .filter { it.isNotEmpty() }
            .doOnNext { log.info("Checking Authentication Cookie `$it`.") }
            .flatMap { authToken ->
                try {
                    Mono.just(tokenProvider.getAuthentication(authToken))
                } catch (_: Exception) {
                    log.warn("Invalid Authentication Cookie `$authToken`.")
                    exchange?.response?.addCookie(tokenProvider.createCookie("", Duration.ZERO))
                    Mono.empty()
                }
            }
    }
}
