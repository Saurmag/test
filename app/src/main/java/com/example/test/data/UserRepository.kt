package com.example.test.data

import com.example.test.entity.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getSortedUserList(): Flow<List<User>>

    fun insertUser(user: User)

    fun deleteUser(user: User)

    fun validateUser(user: User): Deferred<Boolean>
}