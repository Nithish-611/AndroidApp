package com.example.famone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.famone.ui.theme.FamOneTheme
import com.example.famone.ui.theme.composables.LazyStaggeredComposable
import com.example.famone.viewmodel.DocumentViewModel

class ListDocumentsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[DocumentViewModel::class.java]
        val title = intent.getStringExtra("title")

        val isReminder = intent.getBooleanExtra("isReminder", false)
        if(isReminder){
            viewModel.getDocumentByReminder(this)
        }else{
            val category = intent.getStringExtra("category")
            if(category != null){
                viewModel.getDocumentByCategory(this, category)
            }
        }
        setContent {
            FamOneTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.background(Color.Black),
                            title = {
                                Text(text = title?:"")
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    finish()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                        ) {
                            val docList = viewModel.docList.collectAsState().value
                            if(docList.isEmpty()){
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Nothing added yet",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.wrapContentSize()
                                    )
                                }
                            }else {
                                LazyStaggeredComposable(
                                    modifier = Modifier
                                        .padding(top = 56.dp)
                                        .fillMaxSize(),
                                    docList,
                                    context = this@ListDocumentsActivity
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
