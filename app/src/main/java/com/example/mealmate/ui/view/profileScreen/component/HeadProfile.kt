package com.example.mealmate.ui.view.profileScreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mealmate.R
import com.example.mealmate.ui.view.profileScreen.component.localDim.PROFILE_PICTURE

@Composable
fun HeadProfile(
    profilePicture: String? = null,
    username: String? = null,
    onProfilePictureCliked: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(PROFILE_PICTURE)
                .clickable{onProfilePictureCliked(profilePicture)}
            ,
            model = profilePicture,
            contentScale = ContentScale.Crop,
            contentDescription = "User profile picture",
            placeholder = painterResource(R.drawable.nouser_profilepicture)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = username?: "Not define",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                //color = MaterialTheme.colorScheme.onBackground
            ),
            maxLines = 1
        )
    }
}

object localDim{
    val PROFILE_PICTURE = 50.dp
}