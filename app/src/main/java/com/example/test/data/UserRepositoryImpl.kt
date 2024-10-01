package com.example.test.data

import com.example.test.data.database.UserDao
import com.example.test.data.database.UserEntity
import com.example.test.entity.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val configuration: RepositoryConfiguration
): UserRepository {
    override fun getSortedUserList(): Flow<List<User>> =
        userDao.loadSortedUsers()
            .map { users ->
                users.map { it.mapToUser() }
            }

    override fun insertUser(user: User) {
        val userEntity = user.mapToUserEntity()
        configuration.scope.launch(context = configuration.dispatcher) {
            userDao.insertUser(userEntity)
        }
    }

    override fun deleteUser(user: User) {
        val userEntity = user.mapToUserEntity()
        configuration.scope.launch(context = configuration.dispatcher) {
            userDao.deleteUser(userEntity)
        }
    }

    override fun validateUser(user: User): Deferred<Boolean> {
        val isCorrect: Deferred<Boolean> =
            configuration.scope.async(configuration.dispatcher) {
                val userFromDb = userDao.loadUser(name = user.name)
                if (userFromDb.password == user.password) true
                else false
            }
        return isCorrect
    }

    private fun User.mapToUserEntity() =
        UserEntity(
            id = this.id,
            name = this.name,
            birthday = this.birthday,
            password = this.password,
            registerDate = this.registerDate
        )

    private fun UserEntity.mapToUser() =
        User(
            id = this.id,
            name = this.name,
            birthday = this.birthday,
            password = this.password,
            registerDate = this.registerDate
        )
}