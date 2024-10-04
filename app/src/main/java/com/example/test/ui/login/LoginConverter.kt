package com.example.test.ui.login

import com.example.test.entity.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

class LoginConverter {
    fun convert(
        name: String,
        password: String,
        birthday: String
    ): User {
        val registerDate = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat("ddMMyyyy")
        val birthdayDate = simpleDateFormat.parse(birthday) ?: Date(0)
        return User(
            id = UUID.randomUUID(),
            name = name,
            password = password,
            registerDate = registerDate,
            birthday = birthdayDate
        )
    }
}