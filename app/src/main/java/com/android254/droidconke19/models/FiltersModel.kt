package com.android254.droidconke19.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FiltersModel (
        @PrimaryKey
        val id: Int =0,
        val isChecked: Boolean = false,
        val name: String= ""
)
