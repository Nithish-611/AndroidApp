package com.example.famone.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {

    @Upsert
    fun addDocument(document: Document)

    @Delete
    suspend fun deleteDocument(document: Document)

    @Query("SELECT * FROM document ORDER BY date_added")
    fun getDocumentsOrderedbyDate() : List<Document>

    @Query("SELECT * FROM document ORDER BY title ASC")
    fun getDocumentsOrderedbyTitle() : List<Document>

}