package opensource.h3nryc0ding.playground.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {
    @Bean
    @Profile("dev")
    fun corsFilterDev(): FilterRegistrationBean<*> {
        return corsFilter(arrayOf("http://localhost:5173"))
    }

    @Bean
    @Profile("ci")
    fun corsFilterCI(): FilterRegistrationBean<*> {
        return corsFilter(arrayOf("http://localhost"))
    }

    @Bean
    @Profile("prod")
    fun corsFilterProd(): FilterRegistrationBean<*> {
        return corsFilter(arrayOf("https://130.61.237.219.nip.io"))
    }

    private fun corsFilter(urls: Array<String>): FilterRegistrationBean<*> {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        for (url in urls) {
            config.addAllowedOrigin(url)
        }
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return FilterRegistrationBean(CorsFilter(source)).apply {
            this.order = 0
        }
    }
}
