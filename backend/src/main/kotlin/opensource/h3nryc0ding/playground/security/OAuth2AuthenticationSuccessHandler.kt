package opensource.h3nryc0ding.playground.security

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URI

@Component
class OAuth2AuthenticationSuccessHandler : ServerAuthenticationSuccessHandler {
    private val delegate = WebFilterChainServerAuthenticationSuccessHandler()

    override fun onAuthenticationSuccess(
        exchange: WebFilterExchange,
        authentication: Authentication,
    ): Mono<Void> {
        val redirectCookie = exchange.exchange.request.cookies[SecurityConfig.REDIRECT_COOKIE_NAME]?.firstOrNull()
        if (redirectCookie != null) {
            exchange.exchange.response.statusCode = HttpStatus.FOUND
            exchange.exchange.response.headers.location = URI.create(redirectCookie.value)
            return Mono.empty()
        }
        return delegate.onAuthenticationSuccess(exchange, authentication)
    }
}
