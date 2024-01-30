package opensource.h3nryc0ding.playground.config

import opensource.h3nryc0ding.playground.security.JWTHeadersExchangeMatcher
import opensource.h3nryc0ding.playground.security.ReactiveAuthenticationManager
import opensource.h3nryc0ding.playground.security.JWTTokenAuthenticationConverter
import opensource.h3nryc0ding.playground.security.UnauthorizedAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class SecurityConfiguration {
    companion object {
        private val AUTH_WHITELIST =
            arrayOf(
                "/resources/**",
                "/webjars/**",
                "/authorize/**",
                "/favicon.ico",
            )
    }

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        entryPoint: UnauthorizedAuthenticationEntryPoint,
        authFilter: AuthenticationWebFilter,
    ): SecurityWebFilterChain {
        return http {
            httpBasic { disable() }
            formLogin { disable() }
            csrf { disable() }
            logout { disable() }

            exceptionHandling {
                authenticationEntryPoint = entryPoint
            }
            authorizeExchange {
                // TODO
                authorize("/graphql/**", permitAll)
                authorize("/graphiql/**", permitAll)
                authorize("/api/authenticate", permitAll)
                authorize(anyExchange, authenticated)
            }
            addFilterAt(
                authFilter,
                SecurityWebFiltersOrder.AUTHORIZATION,
            )
        }
    }

    @Bean
    fun webFilter(
            reactiveAuthenticationManager: ReactiveAuthenticationManager,
            exchangeMatcher: JWTHeadersExchangeMatcher,
            authConverter: JWTTokenAuthenticationConverter,
    ): AuthenticationWebFilter {
        return AuthenticationWebFilter(reactiveAuthenticationManager).apply {
            setRequiresAuthenticationMatcher(exchangeMatcher)
            setServerAuthenticationConverter(authConverter)
            setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
