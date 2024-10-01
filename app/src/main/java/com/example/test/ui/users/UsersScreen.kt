package com.example.test.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.R
import com.example.test.ui.model.UserModel
import java.util.UUID

@Composable
fun UsersScreen() { TODO() }

@Composable
fun UserList(
    users: List<UserModel>,
    onDeleteClick: (UUID) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        items(users) {
            UserCard(
                name = it.name,
                birthday = it.birthday,
                registerDate = it.registerDate,
                onDeleteClick = { onDeleteClick(it.id) }
            )
        }
    }
}

@Composable
fun UserCard(
    name: String,
    birthday: String,
    registerDate: String,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
    ) {
        Text(
            text = stringResource(id = R.string.username, name),
            modifier = Modifier
                .padding(12.dp)
        )
        Text(
            text = stringResource(id = R.string.birthday, birthday),
            modifier = Modifier
                .padding(12.dp)
        )
        Text(
            text = stringResource(id = R.string.register_date, registerDate),
            modifier = Modifier
                .padding(12.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onDeleteClick() },
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.delete_button),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(device = "id:pixel_5")
@Composable
fun UserCardPreview() {
    val user = UserModel(
        id = UUID.randomUUID(),
        name = "Saurmag",
        birthday = "03-12-2011",
        registerDate = "3 Jun 2015 11:05:30"
    )
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        UserCard(
            name = user.name,
            birthday = user.birthday,
            registerDate = user.registerDate,
            {}
        )
    }
}

@Preview(device = "id:pixel_5")
@Composable
fun UsersPreview() {
    val users = listOf(
        UserModel(
            id = UUID.randomUUID(),
            name = "Saurmag",
            birthday = "03-12-2011",
            registerDate = "3 Jun 2018 22:05:30"
        ),
        UserModel(
            id = UUID.randomUUID(),
            name = "Pharmat",
            birthday = "26-12-2013",
            registerDate = "3 Jun 2015 11:23:30"
        ),
        UserModel(
            id = UUID.randomUUID(),
            name = "Acdaha",
            birthday = "13-01-2015",
            registerDate = "3 Jun 2019 14:05:30"
        ),
    )
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        UserList(users = users, onDeleteClick = {})
    }
}