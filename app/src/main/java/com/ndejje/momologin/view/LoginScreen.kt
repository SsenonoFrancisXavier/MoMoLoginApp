package com.ndejje.momologin.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.animation.*
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

    // 1. Switch for the "Entrance" animation
    var isVisible by remember { mutableStateOf(false) }

    // This handles navigation and flips the entrance switch
    LaunchedEffect(authState) {
        isVisible = true // Trigger the entrance animation immediately
        if (authState is AuthUiState.Success) {
            onLoginSuccess((authState as AuthUiState.Success).username)
            viewModel.resetState()
        }
    }

    // 2. Wrap EVERYTHING in the Entrance Animation
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it / 10 }) + fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))

            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text(stringResource(R.string.label_password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacingSmall)))

            // 3. Animated Error Message (Moves the UI smoothly)
            AnimatedVisibility(
                visible = authState is AuthUiState.Error,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                val message = (authState as? AuthUiState.Error)?.message ?: ""
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.login(usernameInput, passwordInput) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.buttonHeight)),
                enabled = authState !is AuthUiState.Loading
            ) {
                AnimatedContent(
                    targetState = authState is AuthUiState.Loading,
                    label = "button_anim"
                ) { isLoading ->
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(R.string.btn_login))
                    }
                }
            }

            Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))
            TextButton(onClick = onNavigateToRegister) {
                Text(stringResource(R.string.link_register))
            }
        }
    }
}
