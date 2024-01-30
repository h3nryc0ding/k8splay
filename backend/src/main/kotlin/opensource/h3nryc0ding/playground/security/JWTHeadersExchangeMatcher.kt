package opensource.h3nryc0ding.playground.security

import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class JWTHeadersExchangeMatcher : ServerWebExchangeMatcher {
    override fun matches(exchange: ServerWebExchange): Mono<MatchResult> {
        val request = Mono.just(exchange).map { it.request }

        // Check for header "Authorization"
        // TODO: check for cookie
        return request.map(ServerHttpRequest::getHeaders)
            .log()
            .filter { it.containsKey(HttpHeaders.AUTHORIZATION) }
            .flatMap { match() }
            .switchIfEmpty { notMatch() }
    }
}
