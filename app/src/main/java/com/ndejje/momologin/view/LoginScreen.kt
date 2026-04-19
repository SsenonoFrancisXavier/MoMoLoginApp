package com.ndejje.momologin.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ndejje.momologin.R
import com.ndejje.momologin.viewmodel.AuthUiState
import com.ndejje.momologin.viewmodel.AuthViewModel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            onLoginSuccess((authState as AuthUiState.Success).username)
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(dimensionResource(R.dimen.screenPadding)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.label_login),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacingLarge)))

        OutlinedTextField(
            value = usernameInput,
            onValueChange = { usernameInput = it },
            label = { Text(stringResource(R.string.label_username)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))

        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text(stringResource(R.string.label_password)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacingSmall)))

        if (authState is AuthUiState.Error) {
            Text(
                (authState as AuthUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacingSmall)))
        }

        Button(
            onClick = { viewModel.login(usernameInput, passwordInput) },
            modifier = Modifier.fillMaxWidth()
                .height(dimensionResource(R.dimen.buttonHeight)),
            enabled = authState !is AuthUiState.Loading
        ) {
            if (authState is AuthUiState.Loading)
                CircularProgressIndicator(
                    Modifier.size(dimensionResource(R.dimen.spacingLarge)),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            else Text(stringResource(R.string.btn_login))
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))
        TextButton(onClick = onNavigateToRegister) {
            Text(stringResource(R.string.link_register))
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun LoginScreenPreview() {
//    MaterialTheme {
//        // We create a mock layout that looks exactly like your LoginScreen
//        // but doesn't require a real ViewModel to run.
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(dimensionResource(R.dimen.spacingMedium)),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Login",
//                style = MaterialTheme.typography.headlineMedium
//            )
//            Spacer(Modifier.height(dimensionResource(R.dimen.spacingExtraLarge)))
//
//            OutlinedTextField(
//                value = "Xavier_Dev",
//                onValueChange = {},
//                label = { Text("Username") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))
//
//            OutlinedTextField(
//                value = "password123",
//                onValueChange = {},
//                label = { Text("Password") },
//                modifier = Modifier.fillMaxWidth(),
//                visualTransformation = PasswordVisualTransformation()
//            )
//            Spacer(Modifier.height(24.dp))
//
//            Button(
//                onClick = {},
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(dimensionResource(R.dimen.buttonHeight))
//            ) {
//                Text("Login")
//            }
//
//            Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))
//
//            TextButton(onClick = {}) {
//                Text("Don't have an account? Register")
//            }
//        }
//    }
//}