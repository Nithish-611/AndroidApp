package com.example.famone

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.famone.ui.theme.FamOneTheme
import com.example.famone.ui.theme.composables.LazyStaggeredComposable
import com.example.famone.viewmodel.DocumentViewModel
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import kotlin.random.Random

class ListDocumentsActivity : ComponentActivity() {

    private var category = ""
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options = GmsDocumentScannerOptions.Builder()
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .setGalleryImportAllowed(true)
            .setResultFormats(
                GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                GmsDocumentScannerOptions.RESULT_FORMAT_PDF
            )
            .build()
        val scanner = GmsDocumentScanning.getClient(options)

        viewModel = ViewModelProvider(this)[DocumentViewModel::class.java]
        val title = intent.getStringExtra("title")

        val isReminder = intent.getBooleanExtra("isReminder", false)
        if(isReminder){
            viewModel.getDocumentByReminder(this)
        }else{
            category = intent.getStringExtra("category")?:""
            viewModel.getDocumentByCategory(this, category)
        }
        setContent {
            FamOneTheme {
                val scannerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { intent ->
                        if(intent.resultCode == RESULT_OK){
                            val result = GmsDocumentScanningResult.fromActivityResultIntent(intent.data)
                            val imageUris = result?.pages?.map{it.imageUri} ?: emptyList()

                            Intent(this@ListDocumentsActivity, DocumentPreviewActivity::class.java).also{
                                val docName = "Doc"+ Random.nextInt(20);
                                val imgList = viewModel.getImagePathFromUri(ArrayList(imageUris),this)
                                it.putExtra("document_name",docName)
                                it.putExtra("image_list", imgList)
                                if(!isReminder){
                                    it.putExtra("category", category)
                                }

                                ContextCompat.startActivity(this@ListDocumentsActivity, it, null)
                            }
                        }

                    }
                )
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            modifier = Modifier.padding(end = 16.dp),
                            onClick = { },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row {
                                Icon(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            Intent(Intent.ACTION_GET_CONTENT).also {
                                                it.type = "image/*"
                                                it.putExtra(
                                                    Intent.EXTRA_ALLOW_MULTIPLE,
                                                    true
                                                )
                                                startActivityForResult(it, 0)
                                            }
                                        },
                                    painter = painterResource(id = R.drawable.ic_open_gallery_foreground),
                                    contentDescription = "open gallery"
                                )
                                Icon(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            scanner.getStartScanIntent(this@ListDocumentsActivity)
                                                .addOnSuccessListener {
                                                    scannerLauncher.launch(
                                                        IntentSenderRequest.Builder(it).build()
                                                    )
                                                }
                                                .addOnFailureListener{
                                                    Toast.makeText(
                                                        applicationContext,
                                                        it.message,
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        },
                                    painter = painterResource(id = R.drawable.ic_open_camera_foreground),
                                    contentDescription = "open camera"
                                )
                            }

                        }
                    },
                    floatingActionButtonPosition = FabPosition.End,

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode==0){
            val clipData = data?.clipData
            val imageList = ArrayList<Uri>()
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    imageList.add(imageUri)
                }
            } else {
                val singleImageUri = data?.data
                if (singleImageUri != null) {
                    imageList.add(singleImageUri)
                }
            }

            Intent(this@ListDocumentsActivity, DocumentPreviewActivity::class.java).also{
                val docName = "Doc"+ Random.nextInt(20);
                val imgList = viewModel.getImagePathFromUri(imageList,this)
                it.putExtra("document_name",docName)
                it.putExtra("image_list", imgList)
                if(category.isNotEmpty()){
                    it.putExtra("category", category)
                }
                ContextCompat.startActivity(this@ListDocumentsActivity, it, null)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProvider(this@ListDocumentsActivity)[DocumentViewModel::class.java]
        val isReminder = intent.getBooleanExtra("isReminder", false)
        if (isReminder) {
            viewModel.getDocumentByReminder(this)
        } else {
            category = intent.getStringExtra("category") ?: ""
            viewModel.getDocumentByCategory(this, category)
        }
    }
}
