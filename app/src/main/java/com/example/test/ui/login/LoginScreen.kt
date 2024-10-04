package com.example.test.ui.login

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test.R
import com.example.test.ui.design.MaskVisualTransformation
import com.example.test.ui.naviggation.UsersInput
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onTransitionUsers: (UsersInput) -> Unit
) {
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
    val uiState = viewModel.uiState.collectAsState().value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    AuthorizationContent(
        loginUiState = uiState,
        onButtonClick = {
            if (uiState.isAuthorization) {
                if (uiState.name.isBlank() || uiState.password.isBlank()) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.empty_registration_data),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                else {
                    viewModel.authorization(
                        uiState.name,
                        uiState.password,
                        onSuccessAuth = { onTransitionUsers(UsersInput(uiState.name)) },
                        onFailAuth = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.fail_auth),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
            }
            else {
                if (uiState.name.isBlank() || uiState.password.isBlank() || uiState.birthday.isBlank()) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.empty_registration_data),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                else {
                    viewModel.registration(
                        uiState.name,
                        uiState.password,
                        uiState.birthday,
                        onSuccessReg = { onTransitionUsers(UsersInput(uiState.name))},
                        onFailReg = {
                            scope.launch{
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.fail_registration),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
            }
        },
        onTransitionClick = {
            viewModel.changeState { it.copy(isAuthorization = !it.isAuthorization) }
        },
        onNameChange = { name -> viewModel.changeState { it.copy(name = name) } },
        onPasswordChange = { password -> viewModel.changeState { it.copy(password = password) }},
        onBirthdayChange = { birthday -> viewModel.changeState { it.copy(birthday = birthday) } },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun AuthorizationContent(
    loginUiState: LoginUiState,
    onButtonClick: () -> Unit,
    onTransitionClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onBirthdayChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val questionText: String
    val choiceChangeText: String
    val transitionText: String
    if (loginUiState.isAuthorization) {
        questionText = stringResource(id = R.string.registration_question)
        choiceChangeText = stringResource(id = R.string.registration_transition)
        transitionText = stringResource(id = R.string.authorization_transition)
    }
    else {
        questionText = stringResource(id = R.string.authorization_question)
        choiceChangeText = stringResource(id = R.string.authorization_transition)
        transitionText = stringResource(id = R.string.registration_transition)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        EnterName(
            name = loginUiState.name,
            onNameChange = onNameChange
        )
        EnterPassword(
            password = loginUiState.password,
            onPasswordChange = onPasswordChange,
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        )
        AnimatedVisibility(
            visible = !loginUiState.isAuthorization,
            enter = expandVertically(tween(400)),
            exit = shrinkVertically(tween(400))
        ) {
            EnterBirthday(
                birthday = loginUiState.birthday,
                onBirthdayChange = onBirthdayChange,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(text = questionText)
            Text(
                text = choiceChangeText,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onTransitionClick()
                    }
            )
        }
        Button(
            onClick = {
                onButtonClick()
            },
            modifier = Modifier
                .padding(36.dp)
                .fillMaxWidth()
        ) {
            Text(text = transitionText)
        }
    }
}

@Composable
fun EnterName(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = name,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        onValueChange = { onNameChange(it) },
        placeholder = {Text(text = stringResource(id = R.string.register_name))}
    )
}

@Composable
fun EnterPassword(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = password,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        onValueChange = { onPasswordChange(it) },
        placeholder = {Text(text = stringResource(id = R.string.register_password))}
    )
}

@Composable
fun EnterBirthday(
    birthday: String,
    onBirthdayChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = birthday,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        onValueChange = {
            if (it.length <= BirthdayTextField.DATE_LENGTH) { onBirthdayChange(it) }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        visualTransformation = MaskVisualTransformation(BirthdayTextField.DATE_MASK),
        placeholder = {Text(text = stringResource(id = R.string.register_birthday))}
    )
}

object BirthdayTextField {
    const val DATE_MASK = "##-##-####"
    const val DATE_LENGTH = 8
}


@Preview
@Composable
fun AuthorizationPreview() {
    Box(
        modifier = Modifier.background(color = Color.White)
    ) {
        AuthorizationContent(
            loginUiState = LoginUiState(true, "", "", ""),
            onNameChange = {},
            onBirthdayChange = {},
            onPasswordChange = {},
            onButtonClick = {},
            onTransitionClick = {}
        )
    }
}