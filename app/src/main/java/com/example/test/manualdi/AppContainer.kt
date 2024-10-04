package com.example.test.manualdi

import android.content.Context
import androidx.room.Room
import com.example.test.data.RepositoryConfiguration
import com.example.test.data.UserRepository
import com.example.test.data.UserRepositoryImpl
import com.example.test.data.database.AppDatabase
import com.example.test.data.database.UserDao
import com.example.test.data.database.migration_1_2
import com.example.test.data.database.migration_2_3
import com.example.test.data.database.migration_3_4
import com.example.test.ui.login.LoginConverter
import com.example.test.ui.users.UsersConverter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

interface AppContainer {
    val userRepository: UserRepository
    val usersConverter: UsersConverter
    val loginConverter: LoginConverter
}

class DefaultAppContainer(
    context: Context
): AppContainer {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val scope: CoroutineScope = CoroutineScope(context = dispatcher)

    private val configuration = RepositoryConfiguration(dispatcher, scope)

    private val appDatabase: AppDatabase = Room
        .databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "user_database"
        )
        .addMigrations(migration_1_2, migration_2_3, migration_3_4)
        .build()

    private val userDao: UserDao = appDatabase.userDao()

    override val loginConverter: LoginConverter = LoginConverter()

    override val usersConverter: UsersConverter = UsersConverter()

    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(userDao, configuration)
    }
}