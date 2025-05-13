package com.example.mealmate.ui.view.authentification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import coil.compose.AsyncImage
import com.example.mealmate.R
import com.example.mealmate.presentation.navgraph.Routes
import com.example.mealmate.ui.view.authentification.state.Auth_event
import com.example.mealmate.ui.view.authentification.state.Auth_viewModel

@Composable
fun SignUpScreen(
    viewModel: Auth_viewModel = hiltViewModel(),
    navController: NavController,
    onSignInClick: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {}
) {
    LaunchedEffect(key1 = viewModel.uiState.value.successSignUp) {
        if (viewModel.uiState.value.successSignUp) {
            onSignUpSuccess()
        }
    }
    
    val empty by remember { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var c_password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var c_passwordVisibility by rememberSaveable { mutableStateOf(false) }

    val passwordValidation = remember(password) { validatePassword(password) }

    var isPasswordFocused by remember { mutableStateOf(false) }
    val showPasswordRequirements = password.isNotEmpty() || isPasswordFocused
    var loginAttempted by remember { mutableStateOf(false) }

    var validations = remember(email, password, c_password){
        mapOf(
            "userEmail" to isValidEmail(email),
            "userPassword" to passwordValidation.isValid,
            "c_password" to c_password.equals(password),

            "emailEmpty" to email.isEmpty(),
            "passwordEmpty" to password.isEmpty(),
            "c_passwordEmpty" to c_password.isEmpty(),
        )
    }

    //for firebase indicator
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
//        if(vm.inProgress.value){
//            CircularProgressIndicator()
//        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 20.dp, end = 20.dp)
            .verticalScroll(rememberScrollState())
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //logo app
        Image(
            painter = painterResource(id = R.drawable.person_filled_icn),
            contentDescription = "logo app",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(50.dp),
        )
        Text(
            text = "Creez votre compte",
            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
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
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (email.isNotEmpty()) {
                    IconButton(onClick = { email = empty }) {
                        Icon(
                            painter = painterResource(R.drawable.close_icn),
                            contentDescription = null,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
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
            textStyle = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
            ,
            colors = TextFieldDefaults.colors(
                unfocusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.outline,
                //focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                //focusedLabelColor = MaterialTheme.colorScheme.primary, // Label focus
                cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                textColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface // Texte saisi
            ),
        )
        if(!validations["userEmail"]!! && email.isNotBlank()){
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
                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.align(Alignment.Start)
            )
        }else if (validations["emailEmpty"]!!)
        {
            Text(
                text = "Email requis",
                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium.copy(
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

            label = {
                Text(
                    text = stringResource(R.string.password),
                    //color = MaterialTheme.colorScheme.onSurfaceVariant
                ) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.lock_filled_icn),
                    contentDescription = "userPassword",
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
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
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
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
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            textStyle = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onFocusChanged { focuseState ->
                    isPasswordFocused = focuseState.isFocused
                }
            ,
            colors = TextFieldDefaults.colors(
                unfocusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.outline,
                //focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant, // Label non focus
                //focusedLabelColor = MaterialTheme.colorScheme.primary, // Label focus
                cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                textColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface // Texte saisi
            ),
        )
        AnimatedPasswordRequirements(
            password = password,
            showRequirements = showPasswordRequirements,
            modifier = Modifier.fillMaxWidth()
        )
        // Message d'erreur global seulement après tentative de soumission
        if ((loginAttempted && !passwordValidation.isValid) || validations["passwordEmpty"]!!) {
            Text(
                text = "Mot de passe obligatiore",
                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        //userPassword confirmation
        OutlinedTextField(
            value = c_password,
            onValueChange = {newValue->
                c_password = newValue
            },
            label = { Text(
                text = stringResource(R.string.confirm_password),
                //color = MaterialTheme.colorScheme.onSurfaceVariant
            ) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.lock_filled_icn),
                    contentDescription = "userPassword",
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if(c_password.isNotEmpty()){
                    val visibilityIcon =
                        if(c_passwordVisibility){
                            painterResource(R.drawable.visibillity_on_filled_icn)
                        }
                        else{ painterResource(R.drawable.visibility_off_filled_icn)
                        }
                    IconButton(onClick = { c_passwordVisibility = !c_passwordVisibility }){
                        Icon(
                            painter = visibilityIcon,
                            contentDescription = if(c_passwordVisibility) "hide Password" else "Show Password",
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            visualTransformation = if(c_passwordVisibility){
                VisualTransformation.None
            }
            else{
                PasswordVisualTransformation()
            },
            isError = c_password.isNotBlank() && !validations["c_password"]!!,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            textStyle = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onFocusChanged { focuseState ->
                    isPasswordFocused = focuseState.isFocused
                }
            ,
            colors = TextFieldDefaults.colors(
                unfocusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.outline,
                //focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant, // Label non focus
                //focusedLabelColor = MaterialTheme.colorScheme.primary, // Label focus
                cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                textColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface // Texte saisi
            ),
        )
        if ((validations["c_passwordEmpty"]!!) || (!validations["c_password"]!!)) {
            Text(
                text = "Mot de passe non identique",
                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.align(Alignment.Start)
            )
        }

        //signup button
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = {
                loginAttempted = true
                if(passwordValidation.isValid && isValidEmail(email) && validations["c_password"]!!){
                    viewModel.onEvent(Auth_event.OnSignUp(email, password))
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(1.dp, androidx.compose.material3.MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            enabled = passwordValidation.isValid && isValidEmail(email) && validations["c_password"]!!,
        ){
            Text(
                text = "Creer un compte",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }


        //navigation
        //navController.navigate(Routes.Screen.LoginScreen.route
        TextButton(onClick = { onSignInClick() }){
            Text(
                text = "Vous avez deja un compte ? Connectez-vous",
                style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.society_info),
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            ),
        )
        Spacer(modifier = Modifier.height(50.dp))
    }

}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignUpPreview() {
    val navController = rememberNavController()
    SignUpScreen(navController = navController)
}