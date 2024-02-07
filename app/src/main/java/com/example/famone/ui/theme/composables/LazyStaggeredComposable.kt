package com.example.famone.ui.theme.composables

import android.content.Context
import android.content.Intent
import coil.compose.AsyncImage
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.famone.DocumentPreviewActivity
import com.example.famone.data.Document
import java.text.DateFormat
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyStaggeredComposable(
    modifier: Modifier,
    docList:List<Document>,
    context: Context
) {


    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
    ) {
        items(docList) {
            CardItem(item = it,context)
        }
    }

}

@Composable
fun CardItem(item: Document,context: Context) {
    println("ak-- ${item.imageUrl}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(item.cardHeight.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .clickable {
                Intent(context, DocumentPreviewActivity::class.java).also{
                    it.putExtra("document_id",item.documentId)
                    startActivity(context,it,null)
                }
            },
        shape = RoundedCornerShape(15.dp),


        ) {
        Box(modifier = Modifier.fillMaxSize()) {

            item.imageUrl.let {
                val imageUri = it.split(",")[0]
                AsyncImage(modifier = Modifier.fillMaxSize(), model = imageUri, contentDescription = "123")
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 200f
                        )

                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    item.title?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Text(
                        text = DateFormat.getDateTimeInstance().format(item.dateAdded),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic
                        )
                    )
                }

            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}