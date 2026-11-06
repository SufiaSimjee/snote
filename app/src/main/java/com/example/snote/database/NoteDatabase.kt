package com.example.snote.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.snote.model.Note

@Database(entities = [Note::class] , version = 2)
@TypeConverters(NoteTypeConverters::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Note ADD COLUMN sendTo TEXT NOT NULL DEFAULT ''"
        )
    }
}