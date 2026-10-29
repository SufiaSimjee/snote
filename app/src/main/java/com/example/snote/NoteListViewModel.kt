package com.example.snote

import androidx.lifecycle.ViewModel
import com.example.snote.model.Note
import java.util.Date
import java.util.UUID

class NoteListViewModel: ViewModel() {

    val notes = mutableListOf<Note>()

    init {
        for(i in 0 until 100) {
            val note = Note(id = UUID.randomUUID(),
                            title= "Title $i",
                            content = "lorem ispo ",
                            date = Date())

            notes += note
        }
    }
}