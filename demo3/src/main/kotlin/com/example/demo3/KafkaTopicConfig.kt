package com.example.demo3

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfig(@Value("\${kafka.topics.advice}") private val adviceTopic: String) {
    @Bean
    fun adviceTopic(): NewTopic = TopicBuilder
        .name(adviceTopic)
        .partitions(4)
        .replicas(1)
        .build()
}