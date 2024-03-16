package com.example.famone

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.famone.data.Document
import com.example.famone.ui.theme.FamOneTheme
import com.example.famone.ui.theme.composables.CategoryDropDown
import com.example.famone.ui.theme.composables.MyDatePickerDialog
import com.example.famone.ui.theme.composables.MyToolbar
import com.example.famone.utils.DateUtil
import com.example.famone.utils.NotificationUtil
import com.example.famone.viewmodel.DocumentViewModel


class DocumentPreviewActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[DocumentViewModel::class.java]
        var documentName = intent.getStringExtra("document_name")
        val imageListString = intent.getStringExtra("image_list")
        val category = intent.getStringExtra("category")?:"Miscellaneous"
        var imageList = imageListString?.split(",")
        if(imageListString==null){
            val documentId = intent.getIntExtra("document_id", 0)
            viewModel.getDocumentById(this, documentId)
        }





        setContent {
            FamOneTheme {
                var document: Document? = null
                if(imageListString==null){
                    document = viewModel.docList.collectAsState().value[0]
                    val image = document.imageUrl
                    documentName = document.title
                    imageList = image.split(",")
                }




                val pagerState = rememberPagerState(
                    initialPage = 0,
                    initialPageOffsetFraction = 0f,
                    pageCount =
                    {
                        imageList?.size ?: 1
                    }
                )
                val textState = remember { mutableStateOf(TextFieldValue(documentName ?: "")) }
                val keyboardController = LocalSoftwareKeyboardController.current
                val isDateTimePickerVisible = remember { mutableStateOf(false) }
                val isCategoryDropDownVisible = remember { mutableStateOf(false) }
                val selectedCategory = remember { mutableStateOf(document?.categoryType?:category) }
                val date = remember {
                    mutableStateOf(DateUtil.millisToDate(document?.reminderTime))
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.TopCenter,

                    )

                {

                    Column {
                        MyToolbar(title = "") {
                            finish()
                        }
                        if (imageList != null) {
                            HorizontalPager(
                                state = pagerState,
                                key = { imageList!![it] },
                                pageSize = PageSize.Fill,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp, top = 12.dp)
                                    .fillMaxWidth()
                                    .height(200.dp),
                                pageContent = { index ->
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = imageList!![index],
                                        contentDescription = "123"
                                    )
                                }
                            )
                        }
                        // Title
                        OutlinedTextField(
                            value = textState.value,
                            onValueChange = {
                                textState.value = it
                            },
                            label = { Text(text = "Title", color = Color.White) },
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 17.sp
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                }
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background
                            )
                        )

                        // Set reminders
                        Surface(
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .fillMaxWidth(),
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, Color.LightGray),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row( modifier = Modifier
                                .clickable {
                                    isDateTimePickerVisible.value = true
                                }
                                .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(modifier = Modifier.size(24.dp), imageVector = Icons.Outlined.DateRange, contentDescription = "", tint = Color.White)
                                Text(modifier = Modifier.padding(start = 12.dp), text = date.value, fontSize = 17.sp, color = Color.White)
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(modifier = Modifier.size(24.dp), imageVector = Icons.Outlined.Close, contentDescription = "", tint = Color.White)
                            }
                        }

                        //category dropdown
                        Surface(
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .fillMaxWidth(),
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, Color.LightGray),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row( modifier = Modifier
                                .clickable {
                                    isCategoryDropDownVisible.value =
                                        !isCategoryDropDownVisible.value
                                }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(modifier = Modifier.size(24.dp), imageVector = Icons.Outlined.List, contentDescription = "", tint = Color.White)
                                Text(modifier = Modifier.padding(start = 12.dp), text = selectedCategory.value, fontSize = 17.sp, color = Color.White)
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(modifier = Modifier.size(24.dp), imageVector = Icons.Outlined.KeyboardArrowDown, contentDescription = "", tint = Color.White)
                            }
                            CategoryDropDown(isCategoryDropDownVisible, selectedCategory)
                        }

                        //Trigger notification
                        Surface(
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .fillMaxWidth(),
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, Color.LightGray),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row( modifier = Modifier
                                .clickable {
                                    if(NotificationUtil.hasNotificationPermission(this@DocumentPreviewActivity)){
                                        NotificationUtil.triggerDummyNotification(applicationContext)
                                    }else{
                                        val intent = Intent().apply {
                                            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                                            putExtra(Settings.EXTRA_APP_PACKAGE, this@DocumentPreviewActivity.packageName)
                                        }
                                        this@DocumentPreviewActivity.startActivity(intent)
                                    }
                                }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(modifier = Modifier.size(24.dp), imageVector = Icons.Outlined.Notifications, contentDescription = "", tint = Color.White)
                                    Text(modifier = Modifier.padding(start = 12.dp), text = "Trigger Notification", fontSize = 17.sp, color = Color.White)
                                }
                        }

                        //Save button
                        Box(contentAlignment = Alignment.Center) {
                            Button(
                                modifier = Modifier
                                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                onClick = {
                                    if(document != null){
                                        viewModel.upsertDocument(
                                            Document(
                                                documentId = document.documentId,
                                                title = textState.value.text,
                                                dateAdded = document.dateAdded,
                                                imageUrl = TextUtils.join(",", imageList!!),
                                                reminderTime = DateUtil.dateToMillis(date.value),
                                                categoryType = selectedCategory.value
                                            ),this@DocumentPreviewActivity)
                                        finish()
                                    }else{
                                        viewModel.upsertDocument(
                                            Document(
                                                title = textState.value.text,
                                                dateAdded = System.currentTimeMillis(),
                                                imageUrl = TextUtils.join(",", imageList!!),
                                                reminderTime = DateUtil.dateToMillis(date.value),
                                                categoryType = selectedCategory.value
                                            ),this@DocumentPreviewActivity)
                                        finish()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary))
                            {
                                Text(text = "Save", fontSize = 24.sp, color = Color.White)
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