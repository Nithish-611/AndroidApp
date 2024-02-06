package com.example.famone

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.famone.ui.theme.FamOneTheme
import com.example.famone.ui.theme.composables.LazyStaggeredComposable
import com.example.famone.viewmodel.DocumentViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

data class NavigationItem(
    var title: String,
    var selectedIcon: ImageVector,
    var unselectedIcon: ImageVector,
    var badgeCount: Int? = null
)

lateinit var viewModel:DocumentViewModel


class MainActivity : ComponentActivity() {

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProvider(this@MainActivity)[DocumentViewModel::class.java]
        viewModel.retrieveDocumentsByDate(this@MainActivity)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!hasRequiredPermissions()){
            ActivityCompat.requestPermissions(
                this,CAMERAX_PERMISSIONS,0
            )
        }


        setContent {
            FamOneTheme {

                val drawerItems = listOf(

                    NavigationItem(
                        title = "My Profile",
                        selectedIcon = Icons.Filled.Person,
                        unselectedIcon = Icons.Outlined.Person,
                    ),

                    NavigationItem(
                        title = "All Documents",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    NavigationItem(
                        title = "My Reminders",
                        selectedIcon = Icons.Filled.Notifications,
                        unselectedIcon = Icons.Outlined.Notifications,
                    ),
                    NavigationItem(
                        title = "Tools",
                        selectedIcon = Icons.Filled.Create,
                        unselectedIcon = Icons.Outlined.Create,
                    ),
                    NavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                    )
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableStateOf(0)
                    }
                    var expanded by remember{
                        mutableStateOf(false)
                    }
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                drawerItems.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        label = {
                                            Text(text = item.title)
                                        },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            selectedItemIndex = index
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        },
                                        badge = {
                                            item.badgeCount?.let {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }

                            }

                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(
                            modifier = Modifier
                                .nestedScroll(scrollBehaviour.nestedScrollConnection),
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
                                                        it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
                                                    val REQUEST_IMAGE_CAPTURE = 1
                                                    val takePictureIntent =
                                                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                                    try {
                                                        startActivityForResult(
                                                            takePictureIntent,
                                                            REQUEST_IMAGE_CAPTURE
                                                        )
                                                    } catch (e: ActivityNotFoundException) {
                                                        // display error state to the user
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
                                CenterAlignedTopAppBar(
                                    title = {
                                        Text(text = "FamOne")
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "Menu"
                                            )
                                        }
                                    },
                                    actions = {
                                        IconButton(onClick = { /*TODO*/ }) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search"
                                            )
                                        }

                                        IconButton(onClick = { expanded = true  }) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_sortfilter_alt_24),
                                                contentDescription = "SortBy"
                                            )
                                        }

                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("Sort by Name") },
                                                onClick = {
                                                    expanded = false
                                                    viewModel.retrieveDocumentsByName(this@MainActivity)

                                                }
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Sort by Date") },
                                                onClick = {
                                                    expanded = false
                                                    viewModel.retrieveDocumentsByDate(this@MainActivity)

                                                }
                                            )

                                        }
                                    },
                                    scrollBehavior = scrollBehaviour
                                )
                            }
                        ) { values ->
                            val t=values
                            val docList = viewModel.docList.collectAsState().value
                            LazyStaggeredComposable(modifier = Modifier
                                .padding(top = 56.dp)
                                .fillMaxSize(),
                                docList,
                                context = this@MainActivity
                            )
                        }
                    }
                }

            }
        }
    }

    private fun hasRequiredPermissions():Boolean{
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            )==PackageManager.PERMISSION_GRANTED
        }
    }

    companion object{
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode==0){
            val clipData = data?.clipData
            val imageList = ArrayList<Uri>()
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    // Process each image URI here
                    imageList.add(imageUri)
                }
            } else {
                val singleImageUri = data?.data
                if (singleImageUri != null) {
                    imageList.add(singleImageUri)
                }
                // Process the single image URI here
            }

            Intent(this@MainActivity, DocumentPreviewActivity::class.java).also{
                val docName = "Doc"+ Random.nextInt(20);
                val imgList = viewModel.getImagePathFromUri(imageList,this)
                it.putExtra("document_name",docName)
                it.putExtra("image_list", imgList);

                ContextCompat.startActivity(this@MainActivity, it, null)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FamOneTheme {
        Greeting("Android")
    }
}