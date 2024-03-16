package com.example.famone.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.famone.data.Document
import com.example.famone.data.DocumentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


class DocumentViewModel : ViewModel() {

    private val _docList : MutableStateFlow<List<Document>> = MutableStateFlow(emptyList())

    val docList = _docList.asStateFlow()

    fun upsertDocument(document: Document, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            DocumentDatabase.getDatabase(context).dao.addDocument(document)
            retrieveDocumentsByDate(context)
        }
    }

    fun retrieveDocumentsByDate(context: Context) {
        viewModelScope.launch(Dispatchers.IO){
            _docList.value = DocumentDatabase.getDatabase(context).dao.getDocumentsOrderedbyDate()
        }
    }

    fun retrieveDocumentsByName(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            _docList.value = DocumentDatabase.getDatabase(context).dao.getDocumentsOrderedbyTitle()
        }
    }

    fun getDocumentById(context: Context,documentId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            _docList.value = DocumentDatabase.getDatabase(context).dao.getDocumentById(documentId)
        }
    }

    fun getDocumentByCategory(context: Context,category: String){
        viewModelScope.launch(Dispatchers.IO) {
            _docList.value = DocumentDatabase.getDatabase(context).dao.getDocumentByCategory(category)
        }
    }

    fun getDocumentByReminder(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            _docList.value = DocumentDatabase.getDatabase(context).dao.getDocumentByReminder()
        }
    }



    fun getImagePathFromUri(imageList:ArrayList<Uri>, context: Context): String {
        val contentResolver: ContentResolver = context.contentResolver

        val finalImageList = ArrayList<String>()

        for(image in imageList){
            val inputStream = contentResolver.openInputStream(image)

            val tempFile = File.createTempFile("image_${System.currentTimeMillis()}", ".jpg") // Adjust extension as needed

            val outputStream = FileOutputStream(tempFile)

            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.close()
            inputStream.close()
            finalImageList.add(tempFile.absolutePath)
        }


        return TextUtils.join(",",finalImageList)
    }
}