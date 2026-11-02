package com.example.snote.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.snote.model.Note
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
     fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id=(:id)")
    suspend fun getNote(id: UUID): Note

    @Update
    suspend fun updateNote(note: Note)
}