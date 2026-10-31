package com.example.snote

import android.content.Context
import androidx.room.Room
import com.example.snote.model.Note
import com.example.snote.database.NoteDatabase
import kotlinx.coroutines.flow.Flow
import java.util.UUID

private const val DATABASE_NAME = "note_database.db"
class NoteRepository private constructor(context: Context){

    private val database: NoteDatabase = Room.databaseBuilder(
        context.applicationContext,
        NoteDatabase::class.java,
        DATABASE_NAME
    ).createFromAsset(DATABASE_NAME).build()

     fun getNotes(): Flow<List<Note>> = database.noteDao().getNotes()
    suspend fun getNote(id: UUID): Note = database.noteDao().getNote(id)

    companion object {
        private var INSTANCE: NoteRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = NoteRepository(context)
            }
        }

        fun get(): NoteRepository {
            return INSTANCE?:
            throw IllegalStateException("Note repositroy must be initalized")
        }
    }
}