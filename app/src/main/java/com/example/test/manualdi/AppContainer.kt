package com.example.test.manualdi

import android.content.Context
import androidx.room.Room
import com.example.test.data.RepositoryConfiguration
import com.example.test.data.UserRepository
import com.example.test.data.UserRepositoryImpl
import com.example.test.data.database.AppDatabase
import com.example.test.data.database.UserDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

interface AppContainer {
    val userRepository: UserRepository
}

class DefaultAppContainer(
    context: Context
): AppContainer {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val scope: CoroutineScope = CoroutineScope(context = dispatcher)

    private val repositoryConfiguration = RepositoryConfiguration(dispatcher, scope)

    private val appDatabase: AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "user_database"
    ).build()

    private val userDao: UserDao = appDatabase.userDao()

    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(userDao, repositoryConfiguration)
    }
}