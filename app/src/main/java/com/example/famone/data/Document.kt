package com.example.famone.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Document")
data class Document(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "document_id")
    val documentId: Int=0,

    @ColumnInfo(name ="title") val title:String?,
    @ColumnInfo(name ="date_added") val dateAdded:Long,
    @ColumnInfo(name="image_url") val imageUrl: String,


    )
