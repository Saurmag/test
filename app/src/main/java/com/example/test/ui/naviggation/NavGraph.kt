package com.example.test.ui.naviggation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

private const val ROUTE_LOGIN = "login"

private const val ROUTE_USERS = "users/%s"
private const val ARG_USER_NAME = "username"

sealed class NavRoutes(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {

    object Login : NavRoutes(route = ROUTE_LOGIN)

    object Users : NavRoutes(
        route = String.format(ROUTE_USERS, "{$ARG_USER_NAME}"),
        arguments = listOf(navArgument(ARG_USER_NAME){
            type = NavType.StringType
        })
    ) {
        fun routeForUsers(usersInput: UsersInput) =
            String.format(ROUTE_USERS, usersInput.name)

        fun fromEntry(entry: NavBackStackEntry): UsersInput =
            UsersInput(name = entry.arguments?.getString(ARG_USER_NAME) ?: "")
    }
}

data class UsersInput(
    val name: String
)