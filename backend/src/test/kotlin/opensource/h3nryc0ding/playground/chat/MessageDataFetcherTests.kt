package opensource.h3nryc0ding.livechat.chat

import opensource.h3nryc0ding.livechat.generated.types.MessageInput
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.argThat
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class MessageDataFetcherTests {
    @Mock
    private lateinit var messageRepository: MessageRepository

    @InjectMocks
    private lateinit var messageDataFetcher: MessageDataFetcher

    @Test
    fun `getMessages returns all persisted messages`() {
        val (_, message) = mockMessage()
        `when`(messageRepository.findAll()).thenReturn(Flux.just(message))

        val result = messageDataFetcher.messages()

        StepVerifier.create(result)
            .expectNextMatches { it.text == message.text && it.creator == message.creator }
            .verifyComplete()

        verify(messageRepository, times(1)).findAll()
    }

    @Test
    fun `messageSent emits messages sent with messageSend`() {
        val (messageInput, message) = mockMessage()

        `when`(messageRepository.save(any(Message::class.java))).thenReturn(Mono.just(message))

        val result = messageDataFetcher.messageSent()

        StepVerifier.create(result)
            .then { messageDataFetcher.messageSend(messageInput).subscribe() }
            .expectNextMatches { it.text == messageInput.text && it.creator == messageInput.creator }
            .thenCancel()
            .verify()
    }

    @Test
    fun `sendMessage calls save on messageRepository`() {
        val (messageInput, message) = mockMessage()
        `when`(messageRepository.save(any(Message::class.java))).thenReturn(Mono.just(message))

        messageDataFetcher.messageSend(messageInput).subscribe()

        verify(messageRepository, times(1)).save(
            argThat {
                it.text == messageInput.text && it.creator == messageInput.creator
            },
        )
    }

    @Test
    fun `sendMessage returns the saved message`() {
        val (messageInput, message) = mockMessage()
        `when`(messageRepository.save(any(Message::class.java))).thenReturn(Mono.just(message))

        val result = messageDataFetcher.messageSend(messageInput)

        StepVerifier.create(result)
            .expectNextMatches { it.text == message.text && it.creator == message.creator }
            .verifyComplete()
    }

    private fun mockMessage(): Pair<MessageInput, Message> =
        Pair(
            MessageInput(
                text = "Hello",
                creator = "H3nryC0ding",
            ),
            Message(
                text = "Hello",
                creator = "H3nryC0ding",
            ),
        )
}
