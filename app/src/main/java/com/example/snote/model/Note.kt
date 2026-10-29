package com.example.snote.model

import java.util.Date
import java.util.UUID

data class Note(
    val id: UUID,
    val title: String,
    val content: String,
    val date: Date
)
