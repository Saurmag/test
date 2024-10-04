package com.example.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.test.data.UserRepository
import com.example.test.ui.naviggation.NavRoutes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class MainActivityUiState(
    val startDestination: NavRoutes = NavRoutes.Login,
    val nameLoggedUser: String = "",
    val isLoading: Boolean = true
)

class MainActivityViewModel(
    private val repository: UserRepository
): ViewModel() {

    val uiState: StateFlow<MainActivityUiState> =
        combine(
            repository.userIsLogged(),
            repository.getLoggedUsername()
        ) { isLogged, username ->
            val startDestination =
                if (isLogged) NavRoutes.Users
                else NavRoutes.Login
            MainActivityUiState(
                startDestination = startDestination,
                nameLoggedUser = username ?: "",
                isLoading = false
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainActivityUiState(),
            )

    fun logInUser(name: String) {
        repository.loginUser(name)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TestApplication)
                val repository = application.appContainer.userRepository
                MainActivityViewModel(repository)
            }
        }
    }
}