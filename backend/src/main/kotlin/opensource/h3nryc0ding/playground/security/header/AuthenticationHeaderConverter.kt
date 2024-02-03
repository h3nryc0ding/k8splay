package opensource.h3nryc0ding.playground.security.header

import opensource.h3nryc0ding.playground.security.TokenProvider
import opensource.h3nryc0ding.playground.security.TokenProvider.BEARER
import org.slf4j.LoggerFactory
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
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun convert(serverWebExchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange?.request?.headers?.get(HttpHeaders.AUTHORIZATION)?.firstOrNull())
            .filter { it.length > BEARER.length }
            .map { it.substring(BEARER.length) }
            .filter { it.isNotBlank() }
            .doOnNext { log.info("Checking Authentication Header `$it`.") }
            .flatMap { authToken ->
                try {
                    Mono.just(tokenProvider.getAuthentication(authToken))
                } catch (_: Exception) {
                    Mono.empty()
                }
            }
    }
}
