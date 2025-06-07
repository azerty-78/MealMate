package com.example.mealmate.ui.view.components.bottomSheet


import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mealmate.ui.view.components.rowIconText.RowContent
import com.example.mealmate.ui.view.components.rowIconText.RowContentItem
import kotlin.collections.forEachIndexed
import com.example.mealmate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsItemBtmSheet(
    link: String?,
    isVisible: Boolean,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true )
    val listRowContent = listOf(
        RowContentItem(
            value = "Lire sur le site officiel", //dans le navigateur
            icon = R.drawable.outward_arrow_icn,
            onClick = { item->
                link?.let { url->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    }catch (e: Exception){
                        Toast.makeText(context, "Erreur lors de l'ouverture du lien", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(context, "Lien non disponible", Toast.LENGTH_SHORT).show()
                }
                onDismiss()
            },
        )
    )

    if (isVisible){
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState,
            tonalElevation = 4.dp,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color.Gray, CircleShape)
                )
            },
        ){
            ContenairMorvertem(listRowContent = listRowContent)
        }
    }
}

@Composable
fun ContenairMorvertem(
    listRowContent: List<RowContentItem>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp)
) {
    Box( modifier = modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp) ){
        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.Start,
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listRowContent.forEachIndexed { index, item ->
                RowContent(item = item)

                // Ajout d'un Divider entre les éléments sauf pour le dernier
                if (index < listRowContent.lastIndex) {
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }
}