package com.example.famone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        val category = intent.getStringExtra("category")
        val title = intent.getStringExtra("title")

        if(category != null){
            viewModel.getDocumentByCategory(this, category)
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
                                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                                .background(Color.Black)

                        ) {
                            Spacer(modifier = Modifier.height(56.dp))
                            val docList = viewModel.docList.collectAsState().value
                            LazyStaggeredComposable(
                                modifier = Modifier
                                    .padding(top = 56.dp)
                                    .fillMaxSize(),
                                docList,
                                context = this@ListDocumentsActivity
                            )
                        }
                    }
                )
            }
        }
    }
}
