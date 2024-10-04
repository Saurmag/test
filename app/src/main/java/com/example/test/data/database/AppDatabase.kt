package com.example.test.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.test.data.database.entity.LoggedEntity
import com.example.test.data.database.entity.UserEntity
import com.example.test.data.database.entity.UserTypeConverters

@Database(
    entities = [UserEntity::class, LoggedEntity::class],
    version = 4
)
@TypeConverters(UserTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        with(db) {
            execSQL("DROP TABLE IF EXISTS user")
            execSQL("CREATE TABLE \"user\" (\n" +
                    "\t\"name\"\tTEXT NOT NULL,\n" +
                    "\t\"birthday\"\tINTEGER NOT NULL,\n" +
                    "\t\"password\"\tTEXT NOT NULL,\n" +
                    "\t\"register_date\"\tINTEGER NOT NULL,\n" +
                    "\t\"id\"\tBLOB NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\",\"name\")\n" +
                    ");")
        }
    }
}

val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        with(db) {
            execSQL("DROP TABLE IF EXISTS user")
            execSQL("CREATE TABLE \"user\" (\n" +
                    "\t\"name\"\tTEXT NOT NULL UNIQUE,\n" +
                    "\t\"birthday\"\tINTEGER NOT NULL,\n" +
                    "\t\"password\"\tTEXT NOT NULL,\n" +
                    "\t\"register_date\"\tINTEGER NOT NULL,\n" +
                    "\t\"id\"\tBLOB NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\")\n" +
                    ");")
        }
    }
}

val migration_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        with(db) {
            execSQL("CREATE TABLE \"logged\" (\n" +
                    "\t\"user_name\"\tTEXT NOT NULL UNIQUE,\n" +
                    "\t\"id\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                    ");")
            execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_logged_user_name ON logged (user_name)")
        }
    }
}