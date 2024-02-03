package opensource.h3nryc0ding.playground.security

import opensource.h3nryc0ding.playground.user.CustomUser
import opensource.h3nryc0ding.playground.user.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.`when`
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class ReactiveUserDetailsServiceTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userDetailsService: ReactiveUserDetailsService

    @Test
    fun `findByUsername returns user when user is found`() {
        // Arrange
        val login = "testUser"
        val userDetails = CustomUser("testUser", "testUser", "password")
        `when`(userRepository.findByLogin(login)).thenReturn(Mono.just(userDetails))

        // Act
        val result = userDetailsService.findByUsername(login)

        // Assert
        StepVerifier.create(result)
            .expectNext(userDetails.toUserDetails())
            .verifyComplete()
    }

    @Test
    fun `findByUsername returns empty when user not found`() {
        // Arrange
        val login = "testUser"
        `when`(userRepository.findByLogin(login)).thenReturn(Mono.empty())

        // Act
        val result = userDetailsService.findByUsername(login)

        // Assert
        StepVerifier.create(result)
            .verifyComplete()
    }
}
