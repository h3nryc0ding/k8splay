package opensource.h3nryc0ding.playground.security.header

import opensource.h3nryc0ding.playground.security.TokenProvider
import org.springframework.http.HttpHeaders
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationHeaderMatcher : ServerWebExchangeMatcher {
    override fun matches(exchange: ServerWebExchange): Mono<MatchResult> {
        return Mono.justOrEmpty(exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull())
            .filter { it.startsWith(TokenProvider.BEARER) }
            .flatMap { match() }
            .switchIfEmpty(notMatch())
    }
}
