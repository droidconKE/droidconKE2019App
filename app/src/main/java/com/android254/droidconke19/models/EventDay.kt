package com.android254.droidconke19.models

import org.threeten.bp.LocalDate

enum class EventDay {
    Thursday,Friday;

    fun toDate(): LocalDate {
        return when (this) {
            Thursday -> LocalDate.of(2019, 8, 8)
            Friday -> LocalDate.of(2019, 8, 9)
        }
    }
}