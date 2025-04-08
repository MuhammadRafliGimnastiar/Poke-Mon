package com.gimnastiar.pokemon.data.source.local.auth.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntitry
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registUser(user: UserEntitry): Flow<Long>

    @Query("SELECT * FROM user WHERE email = :email AND password = :password LIMIT 1")
    suspend fun loginUser(email: String, password: String): Flow<UserEntitry?>
}