package com.example.mealmate.ui.view.components.bottomSheet

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mealmate.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowIconBtmSheet(
    title: String?,
    value: String?,
    label: String = "Nouvelle valeur",
    isVisible: Boolean,
    @DrawableRes icon: Int,
    keyboardType: KeyboardType,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onValueChange: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var internalValue by rememberSaveable { mutableStateOf(value ?: "") }
    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true )

    if(isVisible){
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState,
            tonalElevation = 4.dp,
            //scrimColor = TODO(), //La couleur de l'arrière-plan estompé qui apparaît derrière la bottom sheet lorsqu'elle est affichée. Par défaut, c'est un noir semi-transparent basé sur
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color.Gray, CircleShape) // Exemple de poignée personnalisée
                )
            }, //permet de remplacer la poignée de glissement par défaut par votre propre composable, ou de la masquer en passant
            //contentWindowInsets = TODO(), //Permet de spécifier comment la bottom sheet gère les insets de la fenêtre (comme la barre de statut, la barre de navigation système).

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { onDismiss() },
                            modifier = Modifier.size(48.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "close btm_sheet",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = internalValue,
                    onValueChange = {
                        internalValue = it
                        onValueChange(it)
                    },
                    label = { Text(label) },
                    leadingIcon = {
                        Icon(painterResource(icon), contentDescription = null)
                    },
                    trailingIcon = {
                        if (internalValue.isNotEmpty()) {
                            IconButton(onClick = { internalValue = "" }) {
                                Icon(
                                    painter = painterResource(R.drawable.close_icn),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = keyboardType
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                //continuer avec google
                OutlinedButton(
                    onClick = {
                        onConfirm(internalValue)
                        //Toast.makeText(context, "Pas de connexion internet", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 2f
                        )
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ){
                    /*Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.google_icn),
                            contentDescription = "Google logo",
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = "Continuer avec Google",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }*/
                    Text(
                        text = "Modifier",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}