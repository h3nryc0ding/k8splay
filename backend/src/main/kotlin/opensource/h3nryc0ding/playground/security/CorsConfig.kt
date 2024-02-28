package opensource.h3nryc0ding.playground.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    /**
     * Frontend URL, including protocol and port
     */
    @Value("\${app.frontend.url}")
    lateinit var frontendUrl: String

    @Bean
    fun corsFilter(): CorsWebFilter {
        val config =
            CorsConfiguration().apply {
                allowCredentials = false
                addAllowedOrigin(frontendUrl)
                addAllowedHeader("*")
                addAllowedMethod("*")
            }
        val source =
            UrlBasedCorsConfigurationSource().apply {
                registerCorsConfiguration("/**", config)
            }
        return CorsWebFilter(source)
    }
}
