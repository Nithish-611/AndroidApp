package com.example.famone.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Document::class],
    version = 1
)
abstract class DocumentDatabase: RoomDatabase() {
    abstract val dao : DocumentDao

    companion object{
        fun getDatabase(context: Context):DocumentDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                DocumentDatabase::class.java, "FamOne_DB"
            ).build()
        }
    }



}