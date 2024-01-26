@file:Suppress("DgsDataSimplifyingInspector", "DgsFieldSimplifyingInspector")

package opensource.h3nryc0ding.playground.chat

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.DgsSubscription
import com.netflix.graphql.dgs.InputArgument
import opensource.h3nryc0ding.playground.generated.types.MessageInput
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.time.LocalDateTime
import java.util.UUID
import opensource.h3nryc0ding.playground.generated.types.Message as MessageDTO

@RedisHash("message")
data class Message(
    @Id
    val id: UUID = UUID.randomUUID(),
    val text: String,
    val creator: String,
    val timestamp: String,
) {
    companion object {
        const val REDIS_PREFIX = "message"
    }

    fun toDTO() =
        MessageDTO(
            id = { this.id.toString() },
            text = { this.text },
            creator = { this.creator },
            timestamp = { this.timestamp },
        )
}

@Suppress("unused")
@Repository
class MessageRepository(
    private val template: ReactiveRedisTemplate<String, Message>,
) {
    private val ops: ReactiveValueOperations<String, Message> = template.opsForValue()

    fun save(message: Message): Mono<Message> {
        return ops.set("${Message.REDIS_PREFIX}:${message.id}", message).map { message }
    }

    fun findById(id: String): Mono<Message> {
        return ops.get("${Message.REDIS_PREFIX}:$id")
    }

    fun findAll(): Flux<Message> {
        return template.scan(ScanOptions.scanOptions().match("${Message.REDIS_PREFIX}:*").count(100).build())
            .flatMap { id -> template.opsForValue().get(id) }
    }

    fun deleteById(id: String): Mono<Boolean> {
        return ops.delete("${Message.REDIS_PREFIX}:$id")
    }
}

@DgsComponent
class MessageDataFetcher(
    private val messageRepository: MessageRepository,
) {
    private val sink = Sinks.many().multicast().directBestEffort<MessageDTO>()

    @DgsQuery(field = "messages")
    fun getMessages(): Flux<MessageDTO> {
        return messageRepository.findAll().map { it.toDTO() }
    }

    @DgsMutation(field = "messageSend")
    fun sendMessage(
        @InputArgument input: MessageInput,
    ): Mono<MessageDTO> {
        val message =
            Message(
                text = input.text,
                creator = input.creator,
                timestamp = LocalDateTime.now().toString(),
            )
        return messageRepository.save(message).map { it.toDTO() }.doOnNext {
            sink.tryEmitNext(it)
        }
    }

    @DgsSubscription(field = "messageSent")
    fun messageSent(): Flux<MessageDTO> {
        return sink.asFlux()
    }
}
