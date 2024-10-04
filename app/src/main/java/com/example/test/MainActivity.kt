package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.ui.login.LoginScreen
import com.example.test.ui.naviggation.NavRoutes
import com.example.test.ui.naviggation.UsersInput
import com.example.test.ui.theme.TestTheme
import com.example.test.ui.users.UsersScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState(isLoading = true))

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        enableEdgeToEdge()
        setContent {
            TestTheme {
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ){ padding ->
                    TestApp(
                        uiState = uiState,
                        snackbarHostState = snackbarHostState,
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.surface)
                            .padding(padding)
                    ) {
                        viewModel.logInUser(it)
                    }
                }
            }
        }
    }
}

@Composable
fun TestApp(
    uiState: MainActivityUiState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onLoginUser: (String) -> Unit
) {
    when(uiState.isLoading) {
        true -> {
            CircularProgressIndicator()
        }
        false -> {
            TestApp(
                nameLoggedUser = uiState.nameLoggedUser,
                snackbarHostState = snackbarHostState,
                startDestination = uiState.startDestination,
                onLoginUser = onLoginUser
            )
        }
    }
}

@Composable
fun TestApp(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState,
    nameLoggedUser: String,
    startDestination: NavRoutes,
    onLoginUser: (String) -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination.route) {
        composable(route = NavRoutes.Login.route) {
            LoginScreen(
                snackbarHostState = snackbarHostState
            ) { username ->
                onLoginUser(username.name)
                navController.navigate(route = NavRoutes.Users.routeForUsers(usersInput = username))
            }
        }
        composable(route = NavRoutes.Users.route) {
            val userInput =
                if (startDestination == NavRoutes.Login) NavRoutes.Users.fromEntry(entry = it)
                else UsersInput(nameLoggedUser)

            UsersScreen(userInput) {
                navController.navigate(route = NavRoutes.Login.route)
            }
        }
    }
}
