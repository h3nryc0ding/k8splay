package opensource.h3nryc0ding.playground

import opensource.h3nryc0ding.playground.user.CustomUser
import opensource.h3nryc0ding.playground.user.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class BackendApplication(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        userRepository.save(CustomUser("user", "user", passwordEncoder.encode("password"))).subscribe()
    }
}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
