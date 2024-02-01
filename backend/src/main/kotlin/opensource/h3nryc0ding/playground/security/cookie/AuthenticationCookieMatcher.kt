package opensource.h3nryc0ding.playground.security.cookie

import opensource.h3nryc0ding.playground.security.TokenProvider
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationCookieMatcher : ServerWebExchangeMatcher {
    override fun matches(exchange: ServerWebExchange): Mono<MatchResult> {
        return Mono.justOrEmpty(exchange.request.cookies[TokenProvider.COOKIE]?.firstOrNull()?.value)
            .filter { it.isNotBlank() }
            .flatMap { match() }
            .switchIfEmpty(notMatch())
    }
}
