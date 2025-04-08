package com.gimnastiar.pokemon.data.source.local.auth.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gimnastiar.pokemon.data.source.local.auth.entity.UserEntitry

@Database(entities = [UserEntitry::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}