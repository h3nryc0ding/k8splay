@file:Suppress("DgsDataSimplifyingInspector", "DgsFieldSimplifyingInspector")

package opensource.h3nryc0ding.playground.chat

import opensource.h3nryc0ding.playground.generated.types.MessageInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.InputArgument
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.DirectProcessor
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.UUID

@RedisHash("message")
data class Message(
        @Id
        val id: UUID = UUID.randomUUID(),
        val text: String,
        val creator: String,
        val timestamp: String,
) {
    fun toDTO() =
            opensource.h3nryc0ding.playground.generated.types.Message(
                    id = { this.id.toString() },
                    text = { this.text },
                    creator = { this.creator },
                    timestamp = { this.timestamp },
            )
}

interface MessageRepository : CrudRepository<Message, Long>

data class MessageSentEvent(val message: Message)

@Component
class MessageSentPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {
    fun publish(message: Message) {
        applicationEventPublisher.publishEvent(MessageSentEvent(message))
    }
}

@DgsComponent
class MessageDataFetcher(
        private val messageRepository: MessageRepository,
        private val messageSentPublisher: MessageSentPublisher,
) {
    private val processor = DirectProcessor.create<Message>().serialize()
    private val flux = processor.sink()

    @EventListener
    fun onMessageSentEvent(event: MessageSentEvent) {
        flux.next(event.message)
    }

    @DgsData(parentType = "Query", field = "messages")
    fun getMessages(): Collection<opensource.h3nryc0ding.playground.generated.types.Message> {
        return messageRepository.findAll().map { it.toDTO() }
    }

    @DgsData(parentType = "Mutation", field = "messageSend")
    fun sendMessage(
            @InputArgument input: MessageInput,
    ): opensource.h3nryc0ding.playground.generated.types.Message {
        val message =
                Message(
                        text = input.text,
                        creator = input.creator,
                        timestamp = LocalDateTime.now().toString(),
                )
        return messageRepository.save(message).let {
            messageSentPublisher.publish(it)
            it.toDTO()
        }
    }

    @DgsData(parentType = "Subscription", field = "messageSent")
    fun messageSent(): Flux<opensource.h3nryc0ding.playground.generated.types.Message> {
        return processor.map { it.toDTO() }
    }
}
