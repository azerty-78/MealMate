package com.example.mealmate.ui.view.communityScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealmate.R
import com.example.mealmate.data.source.model.User
import com.example.mealmate.presentation.navgraph.Routes
import com.example.mealmate.ui.view.components.myBottomAppBar.MyBottomAppBar
import com.example.mealmate.ui.view.components.myBottomAppBar.MyNavigationOnRail
import com.example.mealmate.ui.view.components.myTopAppBar.MyTopBar

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CommunityScreen(
    navController: NavController,
    currentUser: User? = null
) {
    val user = currentUser ?: return

    BoxWithConstraints(modifier = Modifier.fillMaxSize()){
        if(maxWidth > 600.dp){
            Row(modifier = Modifier.fillMaxSize()){
                MyNavigationOnRail(navController = navController)

                MyTopBar(
                    user = user,
                    modifier = Modifier,
                    onNotificationClick = { /*--Rien pour le moment--*/ },
                    onSearchClick = { /*--Rien pour le moment--*/  }
                )
                Spacer(modifier = Modifier.weight(1f))
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
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Routes.Screen.ChatWithAIScreen.route){
                                popUpTo(Routes.Screen.CommunityScreen.route){
                                    saveState = false
                                }
                                launchSingleTop = true
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                    ){
                        Icon(
                            painter = painterResource(R.drawable.chat_with_ai_filled),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                bottomBar = {
                    MyBottomAppBar(navController = navController)
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Bonjour Ben comment vas tu dans Community ?")
                }
            }
        }
    }
}