package com.sorsix.timestampapi.domain.exception

class InvalidDateFormatException(private val date: String) : RuntimeException() {

    override fun toString(): String {
        return "Could not convert string $date to date"
    }
}