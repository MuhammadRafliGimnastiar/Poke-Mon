package com.gimnastiar.pokemon.di

import com.gimnastiar.pokemon.data.AuthRepository
import com.gimnastiar.pokemon.domain.repository.IAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAuthRepository(repo: AuthRepository): IAuthRepository
}