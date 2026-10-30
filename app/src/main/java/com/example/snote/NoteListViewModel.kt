package com.example.snote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snote.model.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class NoteListViewModel: ViewModel() {

    val notes = mutableListOf<Note>()

    init {
        viewModelScope.launch {
            notes += loadNotes()
        }
    }

    suspend fun loadNotes(): List<Note> {
        val result = mutableListOf<Note>()
        delay(5000)
        for (i in 0 until 100) {
            val note = Note(
                id = UUID.randomUUID(),
                title = "Note #$i",
                content = "lorem ispo",
                date = Date()
            )
            result += note
        }
        return result
    }
}