package com.example.mealmate.ui.view.homeScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealmate.data.source.model.User
import com.example.mealmate.ui.view.components.NewsItem.NewsData
import com.example.mealmate.ui.view.components.NewsItem.NewsItem
import com.example.mealmate.ui.view.components.myBottomAppBar.MyBottomAppBar
import com.example.mealmate.ui.view.components.myBottomAppBar.MyNavigationOnRail
import com.example.mealmate.ui.view.components.myTopAppBar.MyTopBar

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(
    navController: NavController,
    currentUser: User? = null
) {
    val user = currentUser ?: return
    val news = listOf(
        NewsData(
            title = "Nouvelle campagne de vaccination pour les diabetiques au centre pasteur !!!",
            authorName = "Ministere de la sante publique",
            link = null,
            partnerImageUrl = null,
            newImageUrl = null,
        ),
        NewsData(
            title = "Nouvelle campagne de vaccination pour les diabetiques au centre pasteur !!!",
            authorName = "Ministere de la sante publique",
            link = null,
            partnerImageUrl = null,
            newImageUrl = null,
        ),
        NewsData(
            title = "Nouvelle campagne de vaccination pour les diabetiques au centre pasteur !!!",
            authorName = "Ministere de la sante publique",
            link = null,
            partnerImageUrl = null,
            newImageUrl = null,
        ),
        NewsData(
            title = "Nouvelle campagne de vaccination pour les diabetiques au centre pasteur !!!",
            authorName = "Ministere de la sante publique",
            link = null,
            partnerImageUrl = null,
            newImageUrl = null,
        ),
        NewsData(
            title = "Nouvelle campagne de vaccination pour les diabetiques au centre pasteur !!!",
            authorName = "Ministere de la sante publique",
            link = null,
            partnerImageUrl = null,
            newImageUrl = null,
        ),
        NewsData(
            title = "Nouvelle campagne de vaccination pour les diabetiques au centre pasteur !!!",
            authorName = "Ministere de la sante publique",
            link = null,
            partnerImageUrl = null,
            newImageUrl = null,
        ),

//        NewsItem(
//            title = "Nouvelle campagne de vaccination pour les diabetiques au centre pasteur !!!",
//            authorName = "Ministere de la sante publique",
//            link = null,
//            partnerImageUrl = null,
//            newImageUrl = null,
//            modifier = Modifier.height(300.dp)
//        )
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