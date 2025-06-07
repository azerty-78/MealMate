package com.example.mealmate.ui.view.components.NewsItem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.mealmate.ui.view.components.bottomSheet.NewsItemBtmSheet
import com.example.mealmate.R

@Composable
fun NewsItem(
    title: String,
    authorName: String,
    link: String?,
    partnerImageUrl: String?,
    newImageUrl: String?,
    contenair: Color = MaterialTheme.colorScheme.background,
    content: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier.height(350.dp)
) {
    var showMoreVert by remember { mutableStateOf(false) }
    var showFullScreenImage by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        //colors = CardDefaults.cardColors(containerColor = contenair) // Définir la couleur du conteneur ici
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier
                .weight(0.8f)
                .clickable { newImageUrl?.let { link -> showFullScreenImage = link } }){
                AsyncImage(
                    model = newImageUrl,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    error = painterResource(R.drawable.ic_launcher_background),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Spacer(modifier = Modifier.width(0.dp))
                AsyncImage(
                    model = partnerImageUrl,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.nouser_profilepicture),
                    error = painterResource(R.drawable.nouser_profilepicture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = content
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {showMoreVert = !showMoreVert},
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "moreVert"
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Icon(
                    painter = painterResource(R.drawable.verified_filled_icn),
                    contentDescription = "Verified Entity",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = authorName,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        showFullScreenImage?.let { imageUrl ->
            Dialog(
                onDismissRequest = { showFullScreenImage = null },
                properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false) // Pour un vrai plein écran
            ) {
                FullScreenImageView(
                    imageUrl = imageUrl,
                    onDismiss = { showFullScreenImage = null }
                )
            }
        }

        if (showMoreVert){
            NewsItemBtmSheet(
                link = link,
                isVisible = showMoreVert,
                onDismiss = { showMoreVert = false}
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun NewsImagePrevv(modifier: Modifier = Modifier) {
    NewsItem(
        title = "Nouvelle campagne de vaccination pour les diabetiques au centre pasteur !!!",
        authorName = "Ministere de la sante publique",
        link = null,
        partnerImageUrl = null,
        newImageUrl = null,
        modifier = Modifier.height(300.dp)
    )

}