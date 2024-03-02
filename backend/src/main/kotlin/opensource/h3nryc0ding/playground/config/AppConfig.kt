package opensource.h3nryc0ding.playground.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.net.URL

@Configuration
class AppConfig {
    @Value("\${app.frontend.url}")
    lateinit var frontendUrl: URL

    @Value("\${app.backend.url}")
    lateinit var backendUrl: URL
}
