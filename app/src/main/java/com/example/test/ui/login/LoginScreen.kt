package com.example.test.ui.login

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.R

@Composable
fun LoginScreen() { TODO() }

@Composable
fun AuthorizationContent(
    onButtonClick: (LoginState) -> Unit,
    modifier: Modifier = Modifier
) {
    var loginState by remember { mutableStateOf(LoginState.Authorization) }
    val questionText: String
    val transitionText: String
    when(loginState) {
        LoginState.Registration -> {
            questionText = stringResource(id = R.string.authorization_question)
            transitionText = stringResource(id = R.string.authorization_transition)
        }
        LoginState.Authorization -> {
            questionText = stringResource(id = R.string.registration_question)
            transitionText = stringResource(id = R.string.registration_transition)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EnterName()
        EnterPassword(
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        )
        AnimatedVisibility(
            visible = loginState == LoginState.Registration,
            enter = expandVertically(tween(400)),
            exit = shrinkVertically(tween(400))
        ) {
            EnterBirthday(
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(text = questionText)
            Text(
                text = transitionText,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        loginState =
                            if (loginState == LoginState.Authorization) LoginState.Registration
                            else LoginState.Authorization
                    }
            )
        }
        Button(
            onClick = {
                onButtonClick(loginState)
            },
            modifier = Modifier
                .padding(36.dp)
                .fillMaxWidth()
        ) {
            Text(text = transitionText)
        }
    }
}

enum class LoginState{
    Registration, Authorization
}

@Composable
fun EnterName(
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    TextField(
        value = name,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        onValueChange = { name = it },
        placeholder = {Text(text = stringResource(id = R.string.register_name))}
    )
}

@Composable
fun EnterPassword(
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }
    TextField(
        value = password,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        onValueChange = { password = it },
        placeholder = {Text(text = stringResource(id = R.string.register_password))}
    )
}

@Composable
fun EnterBirthday(
    modifier: Modifier = Modifier
) {
    var birthday by remember { mutableStateOf("") }
    TextField(
        value = birthday,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        onValueChange = { birthday = it },
        placeholder = {Text(text = stringResource(id = R.string.register_birthday))}
    )
}

@Preview
@Composable
fun AuthorizationPreview() {
    Box(
        modifier = Modifier.background(color = Color.White)
    ) {
        AuthorizationContent({})
    }
}