# ТЕСТОВОЕ ЗАДАНИЕ


### Определение сущности

  ```kotlin

  data class User(
    val id: UUID,
    val name: String,
    val birthday: Date,
    val password: String,
    val registerDate: Date
)

```

### Создание интерфейса репозитория для взаимодействия с БД

Интерфейс:
```kotlin
interface UserRepository
```

Реализацией интерфейса:
```kotlin
class UserRepositoryImpl(
    private val userDao: UserDao,
    private val configuration: RepositoryConfiguration
): UserRepository
```

### Создание простенького Manual DI для простенькой организациия зависимостей

Интерфейс контейнер для внедряемых зависимостей:
```kotlin
interface AppContainer {
    val userRepository: UserRepository
    val usersConverter: UsersConverter
    val loginConverter: LoginConverter
}
```
Реализация интерфейса:
```kotlin
class DefaultAppContainer(context: Context): AppContainer
```

### Некоторые из методов репозитория

Добавление пользователя в БД:
```kotlin
fun insertUser(user: User)
```
Проверка вводимых данных:
```kotlin
fun validateUser(name: String, password: String): Deferred<Boolean>
```
Получение списка отсортированных пользователей
```kotlin
fun getSortedUserList(): Flow<List<User>>
```
Проверка статуса авторизации пользователя
```kotlin
fun userIsLogged(): Flow<Boolean>
```

### Реализация Базы Данных

#### Определение сущностей Базы Данных:

Сущность пользователя:
```kotlin
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
```
Вспомогательный класс для конвертации типов:
```kotlin
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
```
Сущность, хранящая имя авторизовавшегося пользователя:
```kotlin
@Entity(
    tableName = "logged",
    indices = [Index(value = ["user_name"], unique = true)]
)
data class LoggedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "user_name") val userName: String
)
```

 #### Некоторые из запросов в Базе Данных

 Удаление пользователя, при условии, что регистрация удаляемого была выполнена позже авторизованного в данныц момент пользователя:
 ```SQL
DELETE FROM user
WHERE user.name = :deletedUsername AND
user.register_date > (SELECT register_date FROM user WHERE user.name = :authorizedUsername
```
Валидация данных при авторизации:
 ```SQL
SELECT EXISTS(SELECT * FROM user WHERE name = :name AND password = :password)
```
Проверка на доступность имени при регистрации:
```SQL
SELECT EXISTS(SELECT name FROM user WHERE user.name = :name)
```

### Реализация UI

Сущность пользователя для представления 
```kotlin
data class UserModel(
    val id: UUID,
    val name: String,
    val birthday: String,
    val registerDate: String
)
```
ViewModel для контроля статуса авторизация пользователя:
```kotlin
class MainActivityViewModel(
    private val repository: UserRepository
): ViewModel() {

    val uiState: StateFlow<MainActivityUiState> =
    /*... Class ...*/
}

data class MainActivityUiState(
    val startDestination: NavRoutes = NavRoutes.Login,
    val nameLoggedUser: String = "",
    val isLoading: Boolean = true
)
```
ViewModel для экрана Авторизации/Регистрации
```kotlin
class LoginViewModel(
    private val repository: UserRepository,
    private val converter: LoginConverter
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState>
        get() = _uiState.asStateFlow()
    /*... Class ...*/
}
data class LoginUiState(
    val isAuthorization: Boolean = true,
    val name: String = "",
    val password: String = "",
    val birthday: String = ""
)
```
ViewModel для экрана отображения списка пользователей
```kotlin
class UsersViewModel(
    private val repository: UserRepository,
    private val converter: UsersConverter
) : ViewModel() {

    private val _uiState: MutableStateFlow<UsersUiState> = MutableStateFlow(UsersUiState())

    val uiState: StateFlow<UsersUiState>
        get() = _uiState.asStateFlow()
)
    /*... Class ...*/
}
data class UsersUiState(
    val loggedUser: UserModel = UserModel(UUID.randomUUID(), "", "", ""),
    val users: List<UserModel> = emptyList(),
    val isLoading: Boolean = true
)
```
