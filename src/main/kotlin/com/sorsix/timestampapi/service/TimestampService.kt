package com.sorsix.timestampapi.service

import com.sorsix.timestampapi.domain.Timestamp
import com.sorsix.timestampapi.domain.exception.InvalidDateFormatException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Service
class TimestampService {

    val logger: Logger = LoggerFactory.getLogger(TimestampService::class.java)

    fun getCurrentTimestamp(): Timestamp {
        val date = ZonedDateTime.now()
        return getTimestamp(date)
    }

    fun getTimestampFromString(date: String): Timestamp {
        val dateTime = when {
            isLong(date) -> {
                val unix = date.toLong()
                val instant = Instant.ofEpochSecond(unix)
                ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
            }
            isValidDateTime(date) ->
                ZonedDateTime.of(LocalDateTime.parse(date), ZoneOffset.UTC)
            isValidDate(date) ->
                ZonedDateTime.of(LocalDate.parse(date).atStartOfDay(), ZoneOffset.UTC)
            else -> {
                logger.error("Invalid date format [{}]", date)
                throw InvalidDateFormatException(date)
            }
        }
        return getTimestamp(dateTime)
    }

    fun getTimestamp(dateTime: ZonedDateTime): Timestamp {
        return Timestamp(dateTime.toEpochSecond(),
                dateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME).toString())
    }

    fun isLong(date: String): Boolean {
        return try {
            date.toLong()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun isValidDate(date: String): Boolean {
        return try {
            LocalDate.parse(date)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun isValidDateTime(date: String): Boolean {
        return try {
            LocalDateTime.parse(date)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }
}