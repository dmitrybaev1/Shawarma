package ru.shawarma.core.data.datasources

import ru.shawarma.core.data.entities.*
import ru.shawarma.core.data.utils.Result

interface AuthRemoteDataSource {

    suspend fun login(userLoginRequest: UserLoginRequest): Result<AuthData>

    suspend fun refreshToken(tokensRequest: TokensRequest): Result<AuthData>

    suspend fun register(userRegisterRequest: UserRegisterRequest): Result<RegisteredUser>

}