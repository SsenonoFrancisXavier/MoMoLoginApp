package com.ndejje.momologin.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val authState        by viewModel.authState.collectAsState()
    var fullNameInput    by remember { mutableStateOf("") }
    var usernameInput    by remember { mutableStateOf("") }
    var emailInput       by remember { mutableStateOf("") }
    var passwordInput    by remember { mutableStateOf("") }
    var confirmPassInput by remember { mutableStateOf("") }

    // 1. Logic for the entrance animation trigger
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    // Navigation logic (keep this separate from UI)
    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            onRegisterSuccess((authState as AuthUiState.Success).username)
            viewModel.resetState()
        }
    }

    // 2. Wrap the whole screen in an entrance animation
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it / 10 }) + fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.screenPadding)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.label_register),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacingLarge)))

            OutlinedTextField(
                value = fullNameInput,
                onValueChange = { fullNameInput = it },
                label = { Text(stringResource(R.string.label_full_name)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))

            OutlinedTextField(
                value = usernameInput,
                onValueChange = { usernameInput = it },
                label = { Text(stringResource(R.string.label_username)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))

            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text(stringResource(R.string.label_email)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
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
            Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))

            OutlinedTextField(
                value = confirmPassInput,
                onValueChange = { confirmPassInput = it },
                label = { Text(stringResource(R.string.label_confirm_password)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacingSmall)))

            // 3. Animation for the Error Message
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

            // 4. Animation for the Button content
            Button(
                onClick = {
                    viewModel.register(fullNameInput, usernameInput, emailInput, passwordInput, confirmPassInput)
                },
                modifier = Modifier.fillMaxWidth().height(dimensionResource(R.dimen.buttonHeight)),
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
                        Text(stringResource(R.string.btn_register))
                    }
                }
            }

            Spacer(Modifier.height(dimensionResource(R.dimen.spacingMedium)))
            TextButton(onClick = { viewModel.resetState(); onNavigateToLogin() }) {
                Text(stringResource(R.string.link_back_to_login))
            }
        }
    }
}