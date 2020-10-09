package com.sorsix.timestampapi.service

import com.sorsix.timestampapi.domain.Timestamp
import com.sorsix.timestampapi.domain.exception.InvalidDateFormatException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.*
import java.time.format.DateTimeFormatter

internal class TimestampServiceTest {

    private val service = TimestampService()

    @Test
    fun `isValidDate function should return true`() {

        val date = "2020-08-06"

        val actualResult = service.isValidDate(date)

        assertEquals(true, actualResult)
    }

    @Test
    fun `isValidDate function should return false`() {

        val date = "2020-08-32"

        val actualResult = service.isValidDate(date)

        assertEquals(false, actualResult)
    }

    @Test
    fun `isValidDateTime function should return true`() {

        val dateTime = "2020-11-09T11:44:44.797"

        val actualResult = service.isValidDateTime(dateTime)

        assertEquals(true, actualResult)
    }

    @Test
    fun `isValidDateTime function should return false`() {

        val dateTime = "2020/11/09T11:44:44.797"

        val actualResult = service.isValidDateTime(dateTime)

        assertEquals(false, actualResult)
    }

    @Test
    fun `should throw exception on invalid dateTime`() {

        val invalidDateTime = "20-08-0613:20"

        assertThrows<InvalidDateFormatException> {
            service.getTimestampFromString(invalidDateTime)
        }
    }

    @Test
    fun `should throw exception on invalid date`() {

        val invalidDate = "20-03-31"

        assertThrows<InvalidDateFormatException> {
            service.getTimestampFromString(invalidDate)
        }
    }

    @Test
    fun `should not throw exception on valid dateTime`() {

        val dateTime = "2020-08-06T13:20"

        assertDoesNotThrow {
            service.getTimestampFromString(dateTime)
        }
    }

    @Test
    fun `should not throw exception on valid date`() {

        val date = "2020-08-06"

        assertDoesNotThrow {
            service.getTimestampFromString(date)
        }
    }

    @Test
    fun `should get correct timestamp from valid date`() {

        val date = "2020-08-31"
        val dateTime = ZonedDateTime.of(LocalDate.parse(date).atStartOfDay(), ZoneOffset.UTC)
        val expectedResult = Timestamp(dateTime.toEpochSecond(),
                dateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME).toString())

        val actualResult = service.getTimestampFromString(date)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `should get correct timestamp from valid dateTime`() {

        val date = "2020-08-06T13:20"
        val dateTime = ZonedDateTime.of(LocalDateTime.parse(date), ZoneOffset.UTC)
        val expectedResult = Timestamp(dateTime.toEpochSecond(),
                dateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME).toString())

        val actualResult = service.getTimestampFromString(date)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `should throw exception on invalid unix`() {

        val invalidLong = "1596705078 "

        assertThrows<InvalidDateFormatException> {
            service.getTimestampFromString(invalidLong)
        }
    }

    @Test
    fun `should get correct timestamp from unix`() {

        val unix = 1596717724L
        val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(unix), ZoneOffset.UTC)
        val expectedTimestamp = Timestamp(unix,
                dateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME).toString())

        val actualTimestamp = service.getTimestampFromString(unix.toString())

        assertEquals(expectedTimestamp, actualTimestamp)
    }
}