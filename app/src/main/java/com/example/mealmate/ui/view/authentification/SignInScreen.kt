package com.example.mealmate.ui.view.authentification

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mealmate.R
import com.example.mealmate.ui.view.authentification.state.Auth_event
import com.example.mealmate.ui.view.authentification.state.Auth_viewModel
import com.example.mealmate.util.ConnectivityObserver

@Composable
fun SingInScreen(
    viewModel: Auth_viewModel = hiltViewModel(),
    navController: NavController,
    onSignUpClick: () -> Unit = {},
    onForgotPwdClick: () -> Unit = {},
    onSignInSuccess: () -> Unit = {},
    onSignInWithGoogle: () -> Unit = {},
) {
    val uiState by viewModel.uiState
    val context = LocalContext.current
    val networkStatus = viewModel.networkStatus.value

    LaunchedEffect(key1 = uiState.errorSignIn) {
        if (uiState.errorSignIn != null) {
            Toast.makeText(context, uiState.errorSignIn, Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(key1 = networkStatus) {
        if (networkStatus == ConnectivityObserver.Status.Unavailable) {
            Toast.makeText(context, "Pas de connexion internet", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = viewModel.uiState.value.successSignIn){
        if(viewModel.uiState.value.successSignIn){
            onSignInSuccess()
        }
    }

    FullScreenLoader(
        isVisible = uiState.isLoading,
        loadingText = "Authentification en cours..."
    )


    val empty by remember { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    val passwordValidation = remember(password) { validatePassword(password) }

    var isPasswordFocused by remember { mutableStateOf(false) }
    var loginAttempted by remember { mutableStateOf(false) }

    var validations = remember(email, password){
        mapOf(
            "userEmail" to isValidEmail(email),
            "userPassword" to passwordValidation.isValid,
            "emailEmpty" to email.isEmpty(),
            "passwordEmpty" to password.isEmpty(),
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(Modifier.height(100.dp))
        //logo App
        Image(
            painter = painterResource(id = R.drawable.person_filled_icn),
            contentDescription = "logo app",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(50.dp),
        )
        Text(
            text = "Connectez-vous",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(50.dp))


        //userEmail error
        OutlinedTextField(
            value = email,
            onValueChange = { newValue ->
                email = newValue
            },
            label = { Text(
                text = stringResource(R.string.emai),
                //color = MaterialTheme.colorScheme.onSurfaceVariant
            ) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.person_filled_icn),
                    contentDescription = "userEmail",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (email.isNotEmpty()) {
                    IconButton(onClick = { email = empty }) {
                        Icon(
                            painter = painterResource(R.drawable.close_icn),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                }
            },
            isError = email.isNotBlank() && !validations["userEmail"]!!,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
            ,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                //focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                //focusedLabelColor = MaterialTheme.colorScheme.primary, // Label focus
                cursorColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onSurface // Texte saisi
            ),
        )
        if(!validations["userEmail"]!! && email.isNotBlank()) {
            Text(
                text = when {
                    email.count { it == '@' } > 1 -> "Trop de @ dans l'userEmail"
                    '@' !in email -> "L'userEmail doit contenir @"
                    email.split("@").let { it.size != 2 || it[1].count { c -> c == '.' } < 1 } ->
                        "Format de domaine invalide (ex: exemple.com)"
                    email.startsWith('.') || email.endsWith('.') ->
                        "Ne peut pas commencer/finir par un point"
                    else -> "Format d'userEmail invalide"
                },
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.align(Alignment.Start)
            )
        }else if (validations["emailEmpty"]!!)
        {
            Text(
                text = "Email requis",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        //userPassword
        OutlinedTextField(
            value = password,
            onValueChange = {newValue->
                password = newValue
            },
            label = { Text(
                    text = stringResource(R.string.password),
                    //color = MaterialTheme.colorScheme.onSurfaceVariant
                ) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.lock_filled_icn),
                    contentDescription = "userPassword",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if(password.isNotEmpty()){
                    val visibilityIcon =
                        if(passwordVisibility){
                            painterResource(R.drawable.visibillity_on_filled_icn)
                        }
                        else{ painterResource(R.drawable.visibility_off_filled_icn)
                        }
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }){
                        Icon(
                            painter = visibilityIcon,
                            contentDescription = if(passwordVisibility) "hide Password" else "Show Password",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            visualTransformation = if(passwordVisibility){
                VisualTransformation.None
            }
            else{
                PasswordVisualTransformation()
            },
            isError = password.isNotBlank() && !validations["userPassword"]!!,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onFocusChanged { focuseState ->
                    isPasswordFocused = focuseState.isFocused
                }
            ,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                //focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant, // Label non focus
                //focusedLabelColor = MaterialTheme.colorScheme.primary, // Label focus
                cursorColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onSurface // Texte saisi
            ),
        )
        // Message d'erreur global seulement après tentative de soumission
        if ((loginAttempted && !passwordValidation.isValid) || validations["passwordEmpty"]!!) {
            Text(
                text = "Mot de passe obligatiore",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.align(Alignment.Start)
            )
        }

        //navigation
        TextButton(
            onClick = { onForgotPwdClick() },
        ){
            Text(
                text = "Mot de passe oublie ? Changer le.",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        //Button de connexion
        OutlinedButton(
            onClick = {
                loginAttempted = true
                if (viewModel.connectivityObserver.isOnline()){
                    if (passwordValidation.isValid && isValidEmail(email)) {
                        viewModel.onEvent(Auth_event.OnSignIn(email, password))
                    }
                }else {
                    Toast.makeText(context, "Pas de connexion internet", Toast.LENGTH_LONG).show()
                }

            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            enabled = passwordValidation.isValid && isValidEmail(email),
        ){
            Text(
                text = "Se connecter",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }


        // Divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            Text(
                text = "OU",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = MaterialTheme.colorScheme.outline
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        }

        //continuer avec google
        OutlinedButton(
            onClick = {
                /* Google login */
                if(viewModel.connectivityObserver.isOnline()){
                    onSignInWithGoogle()
                }else {
                    Toast.makeText(context, "Pas de connexion internet", Toast.LENGTH_LONG).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledBackgroundColor = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 2f
                )
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
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
            }
        }

        //navigation
        TextButton(
            onClick = { onSignUpClick() },
        ){
            Text(
                text = "Vous n'avez pas de compte ? Creez un compte",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.society_info),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            ),
        )
        Spacer(modifier = Modifier.height(50.dp))

    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    val navController = rememberNavController()
    SingInScreen(navController = navController)

}