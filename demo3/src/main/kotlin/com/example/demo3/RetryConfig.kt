package com.example.demo3

import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.retrytopic.RetryTopicConfiguration
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
import org.springframework.kafka.support.EndpointHandlerMethod
import java.util.concurrent.TimeUnit.SECONDS

@Configuration
class RetryConfig {
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
            // region <hidden solution>
            .traversingCauses(true)
            // endregion
            .create(template)
    }

    companion object : KLogging()
}
