package com.example.famone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.famone.ui.theme.FamOneTheme
import com.example.famone.ui.theme.composables.CardItem
import com.example.famone.utils.DocumentUtil

class AllDocumentsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryList = DocumentUtil.getCategories()

        setContent{
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
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp)
                            .background(Color.Black)

                    ){
                        Spacer(modifier = Modifier.height(56.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            content = {
                                items(categoryList){ item ->
                                    Box (
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .aspectRatio(1f)
                                            .background(
                                                MaterialTheme.colorScheme.primary,
                                                RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ){
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
