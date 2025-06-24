package com.example.mealmate.ui.view.homeScreen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mealmate.data.source.model.User
import com.example.mealmate.ui.view.components.NewsItem.NewsData
import com.example.mealmate.ui.view.components.NewsItem.NewsItem
import com.example.mealmate.ui.view.components.NewsItem.NewsViewModel
import com.example.mealmate.ui.view.components.myBottomAppBar.MyBottomAppBar
import com.example.mealmate.ui.view.components.myBottomAppBar.MyNavigationOnRail
import com.example.mealmate.ui.view.components.myTopAppBar.MyTopBar

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(
    navController: NavController,
    currentUser: User? = null,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val user = currentUser ?: return
    //val newsState by viewModel.newsState.collectAsState()
    val context = LocalContext.current

    // Gestion des toasts
    /*LaunchedEffect(key1 = viewModel.toastMessage) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }*/

    val news = listOf(
        NewsData(
            title = "Couverture sante universselle",
            authorName = "Ministere de la sante publique",
            link = "https://www.facebook.com/share/p/1CB7zMHMPy/",
            partnerImageUrl = null,
            newImageUrl = "https://firebasestorage.googleapis.com/v0/b/mealmate-3cc5c.firebasestorage.app/o/images%2FIMG-20250622-WA0005.jpg?alt=media&token=17bb0c32-a4de-478b-bb72-f5fafcd15b67",
        ),
        NewsData(
            title = "Campagne de santé centre Pasteur",
            authorName = "Centre Pasteur",
            link = "https://www.facebook.com/share/p/1CB7zMHMPy/",
            partnerImageUrl = null,
            newImageUrl = "https://firebasestorage.googleapis.com/v0/b/mealmate-3cc5c.firebasestorage.app/o/images%2FIMG-20250622-WA0003.jpg?alt=media&token=faf35e39-6925-4ed3-b67d-67ae464af96b",
        ),
        NewsData(
            title = "Journées Nationales de Vaccination",
            authorName = "Ministere de la sante publique",
            link = "https://www.facebook.com/share/p/19h2uVyKc2/",
            partnerImageUrl = null,
            newImageUrl = "https://firebasestorage.googleapis.com/v0/b/mealmate-3cc5c.firebasestorage.app/o/images%2FIMG-20250622-WA0002.jpg?alt=media&token=93e5c2db-f598-411d-acf9-c9a0d0c83433",
        ),
        NewsData(
            title = "Nouvelle Campagne de santé ",
            authorName = "Ministere de la sante publique",
            link = "https://www.facebook.com/share/p/1CB7zMHMPy/",
            partnerImageUrl = null,
            newImageUrl ="https://firebasestorage.googleapis.com/v0/b/mealmate-3cc5c.firebasestorage.app/o/images%2FIMG-20250622-WA0001.jpg?alt=media&token=e14a9626-6e8a-4eca-92e7-e87d1a335174",
        ),
        NewsData(
            title = "La sante des meres et des enfants",
            authorName = "Alima",
            link = "https://www.facebook.com/share/p/1CB7zMHMPy/",
            partnerImageUrl = null,
            newImageUrl = "https://firebasestorage.googleapis.com/v0/b/mealmate-3cc5c.firebasestorage.app/o/images%2FIMG-20250622-WA0004.jpg?alt=media&token=47ca1af7-f11a-4549-ad37-bdad1afe500d",
        ),
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()){
        val maxWidth = maxWidth
        if(maxWidth > 600.dp){
            Row(modifier = Modifier.fillMaxSize()){
                val columnCount = when {
                    maxWidth > 1200.dp -> 4
                    else -> 2
                }
                MyNavigationOnRail(navController = navController)
                //other things
                MyTopBar(
                    user = user,
                    modifier = Modifier,
                    onNotificationClick = { /*--Rien pour le moment--*/ },
                    onSearchClick = { /*--Rien pour le moment--*/  }
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columnCount),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    //
                }
            }
        }else{
            Scaffold(
                topBar = {
                    MyTopBar(
                        user = user,
                        modifier = Modifier,
                        onNotificationClick = { /*--Rien pour le moment--*/ },
                        onSearchClick = { /*--Rien pour le moment--*/  }
                    )
                },
                bottomBar = {
                    MyBottomAppBar(navController = navController)
                },
            ) { paddingValues ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    items(news) { news->
                        NewsItem(
                            title = news.title,
                            authorName = news.authorName,
                            link = news.link,
                            partnerImageUrl = news.partnerImageUrl,
                            newImageUrl = news.newImageUrl,
                        )
                    }
                }
            }
        }
    }
}