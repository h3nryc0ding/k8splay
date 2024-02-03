package opensource.h3nryc0ding.playground.security

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class ReactiveAuthenticationManagerTest {
    @Mock
    private lateinit var userDetailsService: ReactiveUserDetailsService

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var reactiveAuthenticationManager: ReactiveAuthenticationManager

    @Test
    fun `authenticate with valid credentials should return Authentication`() {
        // Arrange
        val username = "testUser"
        val password = "testPassword"
        val userDetails = User.withUsername(username).password(password).roles("USER").build()
        val authentication = UsernamePasswordAuthenticationToken(username, password)
        `when`(userDetailsService.findByUsername(username)).thenReturn(Mono.just(userDetails))
        `when`(passwordEncoder.matches(password, userDetails.password)).thenReturn(true)

        // Act
        val result = reactiveAuthenticationManager.authenticate(authentication)

        // Assert
        StepVerifier.create(result)
            .expectNextMatches { it.isAuthenticated }
            .verifyComplete()
    }

    @Test
    fun `authenticate with invalid credentials should return error`() {
        // Arrange
        val username = "testUser"
        val password = "testPassword"
        val userDetails = User.withUsername(username).password(password).roles("USER").build()
        val authentication = UsernamePasswordAuthenticationToken(username, "wrongPassword")
        `when`(userDetailsService.findByUsername(username)).thenReturn(Mono.just(userDetails))
        `when`(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false)

        // Act
        val result = reactiveAuthenticationManager.authenticate(authentication)

        // Assert
        StepVerifier.create(result)
            .expectError(BadCredentialsException::class.java)
            .verify()
    }
}
