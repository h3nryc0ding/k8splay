package opensource.h3nryc0ding.livechat.security

import opensource.h3nryc0ding.livechat.config.AppConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
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
        val GRAPHQL_WHITELIST = arrayOf("/graphql/**", "/graphiql/**", "/subscriptions/**")
    }

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            httpBasic { disable() }
            formLogin { disable() }
            csrf { disable() }

            authorizeExchange {
                authorize(pathMatchers(*GRAPHQL_WHITELIST), permitAll)
                authorize(anyExchange, authenticated)
            }

            oauth2Login {
                authenticationSuccessHandler =
                    RedirectServerAuthenticationSuccessHandler().apply {
                        setLocation(appConfig.FRONTEND_URI)
                    }
            }

            logout {
                requiresLogout = PathPatternParserServerWebExchangeMatcher("/oauth2/revocation/default", HttpMethod.GET)
                logoutHandler = WebSessionServerLogoutHandler()
                logoutSuccessHandler =
                    RedirectServerLogoutSuccessHandler().apply {
                        setLogoutSuccessUrl(appConfig.FRONTEND_URI)
                    }
            }
        }
    }

    @Bean
    fun webSessionIdResolver(): WebSessionIdResolver {
        val resolver = CookieWebSessionIdResolver()
        resolver.addCookieInitializer {
            it.path("/")
            it.httpOnly(true)
            it.domain(appConfig.FRONTEND_URI.host)
            it.secure(true)
            it.sameSite("Lax")
        }
        return resolver
    }
}
