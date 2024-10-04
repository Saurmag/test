package com.example.test.data

import com.example.test.data.database.UserDao
import com.example.test.data.database.entity.LoggedEntity
import com.example.test.data.database.entity.UserEntity
import com.example.test.entity.User
import kotlinx.coroutines.Deferred
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

    override fun deleteUser(nameLoggedUser: String, nameDeletedUser: String) {
        configuration.scope.launch(context = configuration.dispatcher) {
            userDao.deleteUser(nameLoggedUser, nameDeletedUser)
        }
    }

    override fun validateUser(name: String, password: String): Deferred<Boolean> {
        val isCorrect: Deferred<Boolean> =
            configuration.scope.async(configuration.dispatcher) {
                userDao.validateUser(name, password)
            }
        return isCorrect
    }

    override fun getLoggedUsername(): Flow<String?> =
        userDao.loadLoggedUsername()

    override fun loginUser(name: String) {
        val loggedEntity = LoggedEntity(0, name)
        configuration.scope.launch(context = configuration.dispatcher) {
            userDao.insertLogged(loggedEntity)
        }
    }

    override fun logoutUser() {
        configuration.scope.launch(context = configuration.dispatcher) {
            userDao.deleteLogged()
        }
    }

    override fun userIsLogged(): Flow<Boolean> =
        userDao.checkLogged()


    override fun checkUsername(name: String): Deferred<Boolean> {
        val isBusy: Deferred<Boolean> =
            configuration.scope.async(configuration.dispatcher) {
                userDao.checkUsernameExist(name)
            }
        return isBusy
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