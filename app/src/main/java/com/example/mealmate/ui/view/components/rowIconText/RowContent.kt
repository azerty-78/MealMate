package com.example.mealmate.ui.view.components.rowIconText

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mealmate.R
import kotlin.invoke

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowContent(
    item: RowContentItem,
    trailingIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Aller à",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { item.onClick(item) },
                onLongClick = {

                    item.onLongClick?.invoke(item)
                }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.value,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        if(item.title != null){
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                //Spacer(Modifier.height(5.dp))
                Text(
                    text = item.value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }else{
            Text(
                text = item.value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }


        Spacer(modifier = Modifier.weight(1f))
        trailingIcon()
    }
}


@Preview(showBackground = true)
@Composable
fun RowContentPreview() {
    MaterialTheme {
        Column {
            RowContent(
                item = RowContentItem(
                    title = "Le monde",
                    value = "Apparence",
                    icon = R.drawable.p_icn,
                    onClick = {}
                )
            )
            RowContent(
                item = RowContentItem(
                    title = "le monde",
                    value = "Notifications",
                    icon = R.drawable.p_icn,
                    onClick = { println("Notifications cliquées") }
                )
            )
        }
    }
}