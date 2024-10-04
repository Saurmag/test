package com.example.test.ui.users

import com.example.test.entity.User
import com.example.test.ui.model.UserModel
import java.text.DateFormat

class UsersConverter {
    fun convert(userList: List<User>): List<UserModel> {
        return userList.map { user ->
            val birthdayConvert = DateFormat.getDateInstance().format(user.birthday)
            val registerDateConvert = DateFormat.getDateTimeInstance().format(user.registerDate)
            UserModel(
                id = user.id,
                name = user.name,
                birthday = birthdayConvert,
                registerDate = registerDateConvert
            )
        }
    }
}