package com.example.test.data

import com.example.test.entity.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID

interface UserRepository {
    fun getSortedUserList(): Flow<List<User>>

    fun insertUser(user: User)

    fun deleteUser(authorizedUsername: String, deletedUsername: String)

    fun checkUsername(name: String): Deferred<Boolean>

    fun validateUser(name: String, password: String): Deferred<Boolean>

    fun getLoggedUsername(): Flow<String?>

    fun loginUser(name: String)

    fun logoutUser()

    fun userIsLogged(): Flow<Boolean>
}