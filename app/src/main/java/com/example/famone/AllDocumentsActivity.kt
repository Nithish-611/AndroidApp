package com.example.famone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.famone.ui.theme.FamOneTheme
import com.example.famone.utils.DocumentUtil

class AllDocumentsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryList = DocumentUtil.getCategories()

        setContent {
            FamOneTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.background(Color.Black),
                            title = {
                                Text(text = "All Documents")
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
                                .fillMaxSize().background(Color.Black)
                                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(56.dp))
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                content = {
                                    items(categoryList) { item ->
                                        Box(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .clickable {
                                                    Intent(
                                                        this@AllDocumentsActivity,
                                                        ListDocumentsActivity::class.java
                                                    ).also {
                                                        it.putExtra("title", item)
                                                        it.putExtra("category", item)
                                                        ContextCompat.startActivity(
                                                            this@AllDocumentsActivity,
                                                            it,
                                                            null
                                                        )
                                                    }
                                                }
                                                .aspectRatio(1f)
                                                .background(
                                                    MaterialTheme.colorScheme.primary,
                                                    RoundedCornerShape(8.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = item)
                                        }
                                    }
                                }
                            )

                        }

                    }
                )
            }
        }


    }
}
