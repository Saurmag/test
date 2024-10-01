package com.example.test.entity

import java.util.Date
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val birthday: Date,
    val password: String,
    val registerDate: Date
)
