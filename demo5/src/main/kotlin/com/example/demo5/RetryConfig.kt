package com.example.demo5

import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.EXCEPTION
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.EX_CAUSE
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.EX_MSG
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.EX_STACKTRACE
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.GROUP
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.OFFSET
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.PARTITION
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.TOPIC
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.TS
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.TS_TYPE
import org.springframework.kafka.retrytopic.RetryTopicConfiguration
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder
import org.springframework.kafka.retrytopic.RetryTopicConfigurationSupport
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
import org.springframework.kafka.support.EndpointHandlerMethod
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.SECONDS

@Configuration
class RetryConfig :
    RetryTopicConfigurationSupport() { // <- Extend RetryTopicConfigurationSupport class to access CustomizersConfigurer
    @Bean
    fun myOtherRetryTopic(template: KafkaTemplate<String, Int>): RetryTopicConfiguration {
        return RetryTopicConfigurationBuilder
            .newInstance()
            .exponentialBackoff(SECONDS.toMillis(10), 2.0, SECONDS.toMillis(20))
            .maxAttempts(4)
            .retryOn(
                listOf(
                    IllegalArgumentException::class.java
                )
            )
            .autoCreateTopics(true, 4, 1)
            .setTopicSuffixingStrategy(SUFFIX_WITH_INDEX_VALUE) // retry topic name pattern will be: topic_names + -retry- + index e.g.: "advice-topic-retry-0"
            .dltHandlerMethod(
                EndpointHandlerMethod(KafkaConsumer::class.java, KafkaConsumer::consumeDltMessage.name)
            )
            .doNotRetryOnDltFailure()
            .traversingCauses(true)
            .create(template)
    }

    // Let's exclude all headers!
    override fun configureCustomizers(customizersConfigurer: CustomizersConfigurer) {
        customizersConfigurer.customizeDeadLetterPublishingRecoverer {
            it.excludeHeader(
                EX_STACKTRACE,
                EX_CAUSE,
                EX_MSG,
                EXCEPTION,
                TS,
                TS_TYPE,
                PARTITION,
                GROUP,
                TOPIC,
                OFFSET
            )
        }
    }

    // TODO Run service BEFORE expand!
    // region <missing bean>
    @Bean fun taskScheduler(): TaskScheduler = ConcurrentTaskScheduler() // TODO Is it okay to have this kind of Scheduler for Production?
    // endregion

    companion object : KLogging()
}

// region <small example>
fun main() {
    val scheduler: TaskScheduler = ConcurrentTaskScheduler(Executors.newScheduledThreadPool(4)) // TODO change to: 4
    scheduler.scheduleAtFixedRate({ println("0"); Thread.sleep(5000) }, Duration.ofSeconds(2)) // TODO uncomment
    scheduler.scheduleAtFixedRate({ println("1") }, Duration.ofSeconds(2))
    scheduler.scheduleAtFixedRate({ println("2") }, Duration.ofSeconds(2))
    scheduler.scheduleAtFixedRate({ println("3") }, Duration.ofSeconds(2))

    readln()
}
// endregion
