package opensource.h3nryc0ding.playground.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URI

@Component
class OAuth2AuthenticationSuccessHandler : RedirectServerAuthenticationSuccessHandler() {
    /**
     * Frontend URL, including protocol and port
     */
    @Value("\${app.frontend.url}")
    lateinit var frontendUrl: String

    override fun onAuthenticationSuccess(
        exchange: WebFilterExchange,
        authentication: Authentication,
    ): Mono<Void> {
        val request = exchange.exchange.request

        val redirectUri = request.cookies[SecurityConfig.REDIRECT_COOKIE_NAME]?.firstOrNull()?.value
        if (!redirectUri.isNullOrBlank()) {
            this.setLocation(URI.create(redirectUri))
        }
        return Mono.empty()
    }
}
