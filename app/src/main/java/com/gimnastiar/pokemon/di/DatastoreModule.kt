package com.gimnastiar.pokemon.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.gimnastiar.pokemon.data.preferences.SessionDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStorePref: DataStore<Preferences> by preferencesDataStore("PREF_DATASTORE")

@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {

    @Singleton
    @Provides
    fun providesDatastore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStorePref

    @Singleton
    @Provides
    fun providesSessionDatastore(dataStore: DataStore<Preferences>): SessionDatastore =
        SessionDatastore(dataStore)
}