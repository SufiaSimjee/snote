package com.example.snote.database

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

class NoteTypeConverters {

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(value: String): UUID {
        return UUID.fromString(value)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}