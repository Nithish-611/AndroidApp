package com.example.famone.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "Document")
data class Document(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "document_id")
    val documentId: Int=0,

    @ColumnInfo(name ="title")
    val title:String?,

    @ColumnInfo(name="image_url")
    val imageUrl: String,

    @ColumnInfo(name ="date_added")
    val dateAdded:Long,

    @ColumnInfo(name="reminder_time")
    val reminderTime: Long = 0L,

    @ColumnInfo(name="category_type")
    val categoryType: String? = null,

    @ColumnInfo(name="card_height")
    val cardHeight: Int = Random.nextInt(100,150)

)
