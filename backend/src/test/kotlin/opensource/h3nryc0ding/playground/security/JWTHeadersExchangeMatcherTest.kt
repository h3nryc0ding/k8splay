package opensource.h3nryc0ding.playground.security

import opensource.h3nryc0ding.playground.security.JWTHeadersExchangeMatcher
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpHeaders
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class JWTHeadersExchangeMatcherTest {
    @InjectMocks
    private lateinit var jwtHeadersExchangeMatcher: JWTHeadersExchangeMatcher

    @Test
    fun `matches returns match when Authorization header is present`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtHeadersExchangeMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(matchResult.isMatch)
            }
            .verifyComplete()
    }

    @Test
    fun `matches returns notMatch when Authorization header is not a Bearer token`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Basic token")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtHeadersExchangeMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(!matchResult.isMatch)
            }
            .verifyComplete()
    }

    @Test
    fun `matches returns notMatch when Authorization header is absent`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtHeadersExchangeMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(!matchResult.isMatch)
            }
            .verifyComplete()
    }

    @Test
    fun `matches returns notMatch when Authorization header is empty`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtHeadersExchangeMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(!matchResult.isMatch)
            }
            .verifyComplete()
    }

    @Test
    fun `matches returns notMatch when Authorization header is blank`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, " ")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtHeadersExchangeMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(!matchResult.isMatch)
            }
            .verifyComplete()
    }

    @Test
    fun `matches throws error if multiple Authorization headers are set`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token1", "Bearer token2")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtHeadersExchangeMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .expectErrorMatches { throwable ->
                throwable is IllegalArgumentException && throwable.message == "Multiple Authorization headers are not allowed"
            }
            .verify()
    }
}
