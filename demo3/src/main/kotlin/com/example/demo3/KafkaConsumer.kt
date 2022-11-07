package com.example.demo3

import mu.KLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_TOPIC
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class KafkaConsumer {

    private val map = mutableMapOf<Int, Int>()

    @KafkaListener(topics = ["\${kafka.topics.advice}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun consume(
        msg: Int,
        ack: Acknowledgment,
        @Header(RECEIVED_TOPIC) topic: String
    ) {
        logger.info { "======================================================" }
        logger.info { "Received message: $msg from topic: $topic" }

        val attempt = map.getOrPut(msg) { 1 }
        map[msg] = attempt + 1

        logger.info { "================= Attempt: $attempt =========================" }

        if (msg % 2 == 0) {
            logger.info { "Processing FAILED msg: $msg attempt: $attempt" }
            logger.info { "======================================================\n" }
            try {
                nested()
            } catch (e: Exception) {
                throw SuspiciousNumberException(e)
            }
        }

        if (msg % 3 == 0 && attempt != 3) {
            logger.info { "Processing FAILED msg: $msg attempt: $attempt" }
            logger.info { "======================================================\n" }
            try {
                nested()
            } catch (e: Exception) {
                throw SuspiciousNumberException(e)
            }
        }

        logger.info { "Processing SUCCEED for msg: $msg attempt: $attempt" }
        map.remove(msg)
        ack.acknowledge()
        logger.info { "====================================================\n" }
    }

    fun consumeDltMessage(msg: Int, ack: Acknowledgment) {
        logger.warn { "++++++++++++++++++++++++++++++++++++++++++++++++++++" }
        logger.warn { "DLT received message: $msg" }.also { ack.acknowledge() }
        logger.warn { "++++++++++++++++++++++++++++++++++++++++++++++++++++\n" }
    }

    fun nested() {
        throw IllegalArgumentException()
    }

    companion object : KLogging()
}

class SuspiciousNumberException(cause: Throwable? = null) : Throwable(cause)
