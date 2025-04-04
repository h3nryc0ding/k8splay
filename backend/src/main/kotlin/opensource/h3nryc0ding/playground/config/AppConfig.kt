package opensource.h3nryc0ding.livechat.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.net.URI

@Configuration
@SuppressWarnings("ktlint:standard:property-naming")
class AppConfig {
    @Value("\${FRONTEND_URI}")
    lateinit var FRONTEND_URI: URI

    @Value("\${BACKEND_URI}")
    lateinit var BACKEND_URI: URI
}
