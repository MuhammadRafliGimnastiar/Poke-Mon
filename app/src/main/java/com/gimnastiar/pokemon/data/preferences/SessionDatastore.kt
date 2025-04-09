package com.gimnastiar.pokemon.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.utils.Empty
import com.gimnastiar.pokemon.utils.Zero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionDatastore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveSession(user: User, token: String) {
        user.apply {
            dataStore.edit {
                it[ID] = id.toString()
                it[FULL_NAME] = name
                it[EMAIL] = email
                it[TOKEN_KEY] = token
            }
        }
    }

    fun getSession(): Flow<Pair<String, User>> = dataStore.data.map {
        val token = it[TOKEN_KEY] ?: String.Empty
        val user = User(
            id = it[ID]?.toInt() ?: Int.Zero,
            name = it[FULL_NAME] ?: String.Empty,
            email = it[EMAIL] ?: String.Empty
        )
        Pair(token, user)
    }

    suspend fun deleteSession(): Preferences = dataStore.edit {
        it[ID] = String.Empty
        it[FULL_NAME] = String.Empty
        it[EMAIL] = String.Empty
        it[TOKEN_KEY] = String.Empty
    }

    companion object {
        private val ID = stringPreferencesKey("id")
        private val FULL_NAME = stringPreferencesKey("full-name")
        private val EMAIL = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token_key")

    }
}