package opensource.h3nryc0ding.playground.security

import opensource.h3nryc0ding.playground.config.AppConfig
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URI

@Component
class AuthenticationSuccessHandler(
    private val appConfig: AppConfig,
) : RedirectServerAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        exchange: WebFilterExchange,
        authentication: Authentication,
    ): Mono<Void> {
        val request = exchange.exchange.request

        val redirectUri = request.cookies[SecurityConfig.REDIRECT_COOKIE_NAME]?.firstOrNull()?.value
        if (!redirectUri.isNullOrBlank()) {
            this.setLocation(URI.create(redirectUri))
        } else {
            this.setLocation(appConfig.frontendUrl.toURI().resolve("/account"))
        }
        return super.onAuthenticationSuccess(exchange, authentication)
    }
}
