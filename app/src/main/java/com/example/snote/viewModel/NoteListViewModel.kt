package com.example.snote.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snote.NoteRepository
import com.example.snote.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteListViewModel: ViewModel() {
    private val noteRepository = NoteRepository.get()
    private val _notes: MutableStateFlow<List<Note>> = MutableStateFlow(emptyList())
    val notes: StateFlow<List<Note>> get() = _notes.asStateFlow()

    suspend fun addNote(note: Note) {
        noteRepository.addNote(note)
    }

    init {
        viewModelScope.launch {
            noteRepository.getNotes().collect {
                _notes.value = it
            }
        }
    }
}