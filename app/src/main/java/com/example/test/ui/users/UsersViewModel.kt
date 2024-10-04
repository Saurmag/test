package com.example.test.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.test.TestApplication
import com.example.test.data.UserRepository
import com.example.test.ui.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

data class UsersUiState(
    val loggedUser: UserModel = UserModel(UUID.randomUUID(), "", "", ""),
    val users: List<UserModel> = emptyList(),
    val isLoading: Boolean = true
)

class UsersViewModel(
    private val repository: UserRepository,
    private val converter: UsersConverter
) : ViewModel() {

    private val _uiState: MutableStateFlow<UsersUiState> = MutableStateFlow(UsersUiState())

    val uiState: StateFlow<UsersUiState>
        get() = _uiState.asStateFlow()

    fun getSortedUsers(
        authorizedUsername: String
    ) {
        viewModelScope.launch {
            repository.getSortedUserList()
                .map {
                    converter.convert(it)
                }
                .collect { userModelList ->
                    val loggedUser = userModelList.first { it.name == authorizedUsername }
                    val filteredUsers = userModelList.filter { it.name != authorizedUsername}
                    _uiState.value = UsersUiState(
                        loggedUser = loggedUser,
                        users = filteredUsers,
                        isLoading = false
                    )
                }
        }
    }

    fun deleteUser(nameLoggedUser: String, nameDeletedUser: String) {
        viewModelScope.launch {
            repository.deleteUser(nameLoggedUser, nameDeletedUser)
        }
    }

    fun logOut() {
        repository.logoutUser()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TestApplication)
                val converter = application.appContainer.usersConverter
                val repository = application.appContainer.userRepository
                UsersViewModel(repository, converter)
            }
        }
    }
}