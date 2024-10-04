package com.example.test.data

import com.example.test.entity.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getSortedUserList(): Flow<List<User>>

    fun insertUser(user: User)

    fun deleteUser(nameLoggedUser: String, nameDeletedUser: String)

    fun checkUsername(name: String): Deferred<Boolean>

    fun validateUser(name: String, password: String): Deferred<Boolean>

    fun getLoggedUsername(): Flow<String?>

    fun loginUser(name: String)

    fun logoutUser()

    fun userIsLogged(): Flow<Boolean>
}