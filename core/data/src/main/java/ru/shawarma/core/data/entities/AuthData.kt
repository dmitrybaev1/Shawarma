package ru.shawarma.core.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long
): Parcelable{
    companion object{
        fun empty(): AuthData = AuthData("","","",0)
    }

    fun isEmpty(): Boolean =
        this == empty()

}
