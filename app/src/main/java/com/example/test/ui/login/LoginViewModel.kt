package com.example.test.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.test.TestApplication
import com.example.test.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isAuthorization: Boolean = true,
    val name: String = "",
    val password: String = "",
    val birthday: String = ""
)

class LoginViewModel(
    private val repository: UserRepository,
    private val converter: LoginConverter
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState>
        get() = _uiState.asStateFlow()

    fun changeState(onChange: (LoginUiState) -> LoginUiState) {
        _uiState.update { uiState ->
            onChange(uiState)
        }
    }

    fun registration(
        name: String,
        password: String,
        birthday: String,
        onSuccessReg: () -> Unit,
        onFailReg: () -> Unit
    ) {
        viewModelScope.launch {
            val isBusy = repository.checkUsername(name).await()
            if (isBusy){
                onFailReg()
            }
            else {
                val user = converter.convert(name, password, birthday)
                repository.insertUser(user)
                onSuccessReg()
            }
        }
    }

    fun authorization(
        name: String,
        password: String,
        onSuccessAuth: () -> Unit,
        onFailAuth: () -> Unit
    ) {
        viewModelScope.launch {
            val isCorrectData = repository.validateUser(name, password).await()
            if (isCorrectData) onSuccessAuth()
            else onFailAuth()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TestApplication)
                val converter = application.appContainer.loginConverter
                val repository = application.appContainer.userRepository
                LoginViewModel(repository, converter)
            }
        }
    }
}