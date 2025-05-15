package com.example.mealmate.ui.view.profileScreen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mealmate.R
import com.example.mealmate.data.source.model.User
import com.example.mealmate.ui.view.components.rowIconText.RowContentItem

@Composable
fun ContenairProfileItem(
    currentUser: User? = null,
    modifier: Modifier = Modifier
) {
    val user = currentUser?: return

    val profileItems = listOf(
        RowContentItem(
            title = user.username.toString(),
            icon = R.drawable.person_filled_icn,
            onClick = { /*Open bottomSheet for update userInformations*/ }
        ),
        RowContentItem(
            title = user.password,
            icon = R.drawable.lock_filled_icn,
            onClick = { /*Open bottomSheet for update userInformations*/ }
        ),
        RowContentItem(
            title = user.username.toString(),
            icon = R.drawable.person_filled_icn,
            onClick = { /*Open bottomSheet for update userInformations*/ }
        ),
        RowContentItem(
            title = user.username.toString(),
            icon = R.drawable.person_filled_icn,
            onClick = { /*Open bottomSheet for update userInformations*/ }
        ),
        RowContentItem(
            title = user.username.toString(),
            icon = R.drawable.person_filled_icn,
            onClick = { /*Open bottomSheet for update userInformations*/ }
        ),
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ,
        horizontalAlignment = Alignment.Start
    ) {

    }

}