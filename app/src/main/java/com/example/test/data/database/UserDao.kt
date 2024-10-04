package com.example.test.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test.data.database.entity.LoggedEntity
import com.example.test.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY register_date")
    fun loadSortedUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT EXISTS(SELECT name FROM user WHERE user.name = :name)")
    suspend fun checkUsernameExist(name: String): Boolean

    @Query("DELETE FROM user \n" +
            "WHERE user.name = :deletedUsername AND \n" +
            "user.register_date > (SELECT register_date FROM user WHERE user.name = :authorizedUsername)")
    suspend fun deleteUser(authorizedUsername: String, deletedUsername: String)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE name = :name AND password = :password)")
    suspend fun validateUser(name: String, password: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogged(loggedEntity: LoggedEntity)

    @Query("SELECT EXISTS(SELECT * FROM logged)")
    fun checkLogged(): Flow<Boolean>

    @Query("DELETE FROM logged")
    suspend fun deleteLogged()

    @Query("SELECT user_name FROM logged")
    fun loadLoggedUsername(): Flow<String>
}