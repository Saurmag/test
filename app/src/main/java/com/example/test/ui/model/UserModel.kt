package com.example.test.ui.model

import java.util.UUID

data class UserModel(
    val id: UUID,
    val name: String,
    val birthday: String,
    val registerDate: String
)
