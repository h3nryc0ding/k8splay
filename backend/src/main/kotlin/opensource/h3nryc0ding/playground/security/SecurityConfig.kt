package opensource.h3nryc0ding.playground.security

import opensource.h3nryc0ding.playground.config.AppConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers
import org.springframework.web.server.session.CookieWebSessionIdResolver
import org.springframework.web.server.session.WebSessionIdResolver

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class SecurityConfig(
    private val appConfig: AppConfig,
) {
    companion object {
        val GRAPHQL_WHITELIST = arrayOf("/graphql/**", "/graphiql/**")
        const val REDIRECT_COOKIE_NAME = "REDIRECT_URI"
        const val REDIRECT_PARAM_NAME = "redirect_uri"
    }

    @Bean
    fun securityFilterChain(
        http: ServerHttpSecurity,
        customServerAuthorizationRequestRepository: CustomServerAuthorizationRequestRepository,
        authenticationSuccessHandler: AuthenticationSuccessHandler,
    ): SecurityWebFilterChain {
        return http {
            httpBasic { disable() }
            formLogin { disable() }
            csrf { disable() }

            authorizeExchange {
                authorize(pathMatchers(*GRAPHQL_WHITELIST), permitAll)
                authorize(anyExchange, authenticated)
            }

            oauth2Login {
                authorizationRequestRepository = customServerAuthorizationRequestRepository
                this.authenticationSuccessHandler = authenticationSuccessHandler
            }

            logout {
                logoutSuccessHandler = RedirectServerLogoutSuccessHandler().apply {
                    setLogoutSuccessUrl(appConfig.frontendUrl.toURI())
                }
            }
        }
    }

    @Bean
    fun webSessionIdResolver(): WebSessionIdResolver {
        val resolver = CookieWebSessionIdResolver()
        resolver.cookieName = "SESSION"
        resolver.addCookieInitializer {
            it.path("/")
            it.httpOnly(true)
            it.domain(appConfig.frontendUrl.host)
            it.secure(true)
            it.sameSite("Lax")
        }
        return resolver
    }
}
