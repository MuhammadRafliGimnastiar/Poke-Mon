package com.gimnastiar.pokemon.di

import com.gimnastiar.pokemon.domain.usecase.auth.AuthInteractor
import com.gimnastiar.pokemon.domain.usecase.auth.AuthUseCase
import com.gimnastiar.pokemon.domain.usecase.core.CoreInteractor
import com.gimnastiar.pokemon.domain.usecase.core.CoreUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun provideAuthUseCasepro(interactor: AuthInteractor) : AuthUseCase

    @Binds
    @Singleton
    abstract fun provideCoreUseCasepro(interactor: CoreInteractor) : CoreUseCase
}