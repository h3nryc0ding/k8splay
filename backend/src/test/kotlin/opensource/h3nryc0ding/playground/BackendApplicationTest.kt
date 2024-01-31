package opensource.h3nryc0ding.playground

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.ApplicationContext
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTest {
    companion object {
        @Container
        @ServiceConnection
        val mongo = MongoDBContainer("mongo:latest")
    }

    @Test
    fun contextLoads(applicationContext: ApplicationContext?) {
        assertNotNull(applicationContext)
    }

    @Test
    fun `mongo is running`() {
        assertTrue(mongo.isRunning)
    }
}
