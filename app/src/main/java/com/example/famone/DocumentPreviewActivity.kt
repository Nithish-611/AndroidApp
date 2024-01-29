package com.example.famone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.famone.ui.theme.FamOneTheme
import com.example.famone.ui.theme.composables.MyDatePickerDialog
import com.example.famone.viewmodel.DocumentViewModel

class DocumentPreviewActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[DocumentViewModel::class.java]

        val documentId = intent.getIntExtra("document_id", 0)
        viewModel.getDocumentById(this, documentId)



        setContent {
            FamOneTheme {

                val document = viewModel.docList.collectAsState().value[0]
                val image = document.imageUrl
                val imageList = image.split(",")


                val pagerState = rememberPagerState()
                val textState = remember { mutableStateOf(TextFieldValue(document.title ?: "")) }

                val isDateTimePickerVisible = remember { mutableStateOf(false) }
                val date = remember {
                    mutableStateOf("Set reminder")
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.TopCenter,

                    )

                {

                    Column {
                        HorizontalPager(
                            pageCount = imageList.size,
                            state = pagerState,
                            key = { imageList[it] },
                            pageSize = PageSize.Fill,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 100.dp)
                                .fillMaxWidth()
                                .height(360.dp)
                        ) { index ->
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = imageList[index],
                                contentDescription = "123"
                            )
                        }

                        TextField(
                            value = textState.value,
                            onValueChange = {
                                textState.value = it
                            },
                            label = { Text(text = "Document Title") },
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 24.sp
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedLabelColor = Color.DarkGray,
                                unfocusedLabelColor = Color.Black,
                                focusedIndicatorColor = Color.Black
                            )
                        )
                        Box(contentAlignment = Alignment.Center) {
                            Button(
                                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                                onClick = { isDateTimePickerVisible.value = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray))
                            {
                                Text(text = date.value, fontSize = 24.sp)
                            }
                        }

                        if (isDateTimePickerVisible.value) {
                            MyDatePickerDialog(
                                onDateSelected = {
                                    if(it.isNotEmpty()) {
                                        date.value = it
                                    }
                                },
                                onDismiss = { isDateTimePickerVisible.value = false }
                            )
                        }
                    }
                }
            }
        }
    }
}