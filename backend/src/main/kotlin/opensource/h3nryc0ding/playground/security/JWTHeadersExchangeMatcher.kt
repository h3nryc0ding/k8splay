package opensource.h3nryc0ding.playground.security

import opensource.h3nryc0ding.playground.security.TokenProvider.BEARER
import org.springframework.http.HttpHeaders
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JWTHeadersExchangeMatcher : ServerWebExchangeMatcher {
    override fun matches(exchange: ServerWebExchange): Mono<MatchResult> {
        return Mono.just(exchange)
            .log()
            .map { it.request.headers }
            .flatMap { headers ->
                if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                    val authHeaders = headers[HttpHeaders.AUTHORIZATION]!!
                    if (authHeaders.size > 1) {
                        // TODO: map status code to 400
                        return@flatMap Mono.error(IllegalArgumentException("Multiple Authorization headers are not allowed"))
                    } else if (authHeaders.isNotEmpty() && authHeaders[0].startsWith(BEARER)) {
                        return@flatMap match()
                    }
                }
                notMatch()
            }
    }
}
