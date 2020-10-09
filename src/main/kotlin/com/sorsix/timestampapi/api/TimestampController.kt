package com.sorsix.timestampapi.api

import com.sorsix.timestampapi.domain.Timestamp
import com.sorsix.timestampapi.domain.exception.InvalidDateFormatException
import com.sorsix.timestampapi.service.TimestampService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/timestamp")
class TimestampController(val service: TimestampService) {

    @GetMapping("/{dateString}")
    fun getTimestamp(@PathVariable dateString: String): Timestamp {
        return service.getTimestampFromString(dateString)
    }

    @GetMapping
    fun getCurrentTimestamp(): Timestamp {
        return service.getCurrentTimestamp()
    }

    @ExceptionHandler(InvalidDateFormatException::class)
    fun invalidDateFormatHandler(e: InvalidDateFormatException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.badRequest().body(mapOf("error" to e.toString()))
    }
}