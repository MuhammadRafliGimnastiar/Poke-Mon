package com.gimnastiar.pokemon.di

import android.content.Context
import androidx.room.Room
import com.gimnastiar.pokemon.data.source.local.auth.room.UserDao
import com.gimnastiar.pokemon.data.source.local.auth.room.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java, "user.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesUserDao(database: UserDatabase): UserDao = database.userDao()
}