package opensource.h3nryc0ding.playground.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {
    @Value("\${PUBLIC_FRONTEND_DOMAIN}")
    lateinit var baseDomain: String

    private fun createCorsFilter(origin: String): FilterRegistrationBean<*> {
        val source = UrlBasedCorsConfigurationSource()
        val config =
            CorsConfiguration().apply {
                allowCredentials = true
                addAllowedOrigin(origin)
                addAllowedHeader("*")
                addAllowedMethod("*")
            }
        source.registerCorsConfiguration("/**", config)
        return FilterRegistrationBean(CorsFilter(source)).apply {
            this.order = 0
        }
    }

    @Bean
    @Profile("prod")
    fun corsFilterProd(): FilterRegistrationBean<*> {
        return createCorsFilter("https://$baseDomain")
    }

    @Bean
    @Profile("!prod")
    fun corsFilter(): FilterRegistrationBean<*> {
        @Suppress("HttpUrlsUsage")
        return createCorsFilter("http://$baseDomain")
    }
}
