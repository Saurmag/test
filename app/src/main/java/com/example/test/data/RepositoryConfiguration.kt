package com.example.test.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class RepositoryConfiguration(
    val dispatcher: CoroutineDispatcher,
    val scope: CoroutineScope
)