package com.example.mealmate.ui.view.myTopAppBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mealmate.ui.view.myTopAppBar.DimensionTopBar.ICON_BUTTON_SIZE
import com.example.mealmate.ui.view.myTopAppBar.DimensionTopBar.ICON_SIZE
import com.example.mealmate.ui.view.myTopAppBar.DimensionTopBar.PROFILE_PICTURE_SIZE

import com.example.mealmate.R
import com.example.mealmate.data.source.model.User

@Composable
fun MyTopBar(
    user: User,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 30.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            .background(MaterialTheme.colorScheme.background)
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(PROFILE_PICTURE_SIZE),
            contentAlignment = Alignment.Center
        ){
            AsyncImage(
                modifier = modifier.clip(CircleShape),
                model = user.profilePictureUrl,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.nouser_profilepicture),
            )
        }

        Column(
            modifier = Modifier.weight(1f).padding(start = 10.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text ="Salut !",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = user.username?: "Not define",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
            )
        }

        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ){
            IconButton(
                onClick = { onSearchClick() },
                modifier = Modifier.size(ICON_BUTTON_SIZE)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(ICON_SIZE)
                )
            }
            IconButton(
                onClick = { onNotificationClick() },
                modifier = Modifier.size(ICON_BUTTON_SIZE)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(ICON_SIZE)
                )
            }
        }

    }

}

object DimensionTopBar{
    val PROFILE_PICTURE_SIZE = 40.dp
    val ICON_BUTTON_SIZE = 40.dp
    val ICON_SIZE = 24.dp
}