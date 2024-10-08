package com.example.test.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "user",
    indices = [Index(value = ["name"], unique = true)]
)
data class UserEntity(
    @PrimaryKey
    val id: UUID,
    val name: String,
    val birthday: Date,
    val password: String,
    @ColumnInfo(name = "register_date")
    val registerDate: Date
)

class UserTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millis: Long): Date {
        return Date(millis)
    }
}
