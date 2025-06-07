package com.example.mealmate.ui.view.settingsScreen.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mealmate.ui.view.components.rowIconText.RowContent
import com.example.mealmate.ui.view.components.rowIconText.RowContentItem
import com.example.mealmate.ui.view.components.rowIconText.myList1

@Composable
fun ContenairSettingsItems(
    listRowContent: List<RowContentItem>,
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp) // Ajout de padding pour les bords
        ,
        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onSurface),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.Start,
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

@Preview
@Composable
private fun CardContentPrev() {
    ContenairSettingsItems(myList1)
}