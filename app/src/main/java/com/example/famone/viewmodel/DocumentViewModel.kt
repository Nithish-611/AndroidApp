package com.example.famone.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.famone.data.Document
import com.example.famone.data.DocumentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class DocumentViewModel : ViewModel() {

    private val _docList : MutableStateFlow<List<Document>> = MutableStateFlow(emptyList())

    val docList = _docList.asStateFlow()

    fun upsertDocument(document: Document, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            DocumentDatabase.getDatabase(context).dao.addDocument(document)
            retrieveDocuments(context)
        }
    }

    fun retrieveDocuments(context: Context) {
        viewModelScope.launch(Dispatchers.IO){
            _docList.value = DocumentDatabase.getDatabase(context).dao.getDocumentsOrderedbyDate()
        }
    }



}