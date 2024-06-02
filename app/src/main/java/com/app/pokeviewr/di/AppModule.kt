package com.app.pokeviewr.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.app.pokeviewr.db.PokemonDatabase
import com.app.pokeviewr.network.ApiService
import com.app.pokeviewr.repository.MainRepository
import com.app.pokeviewr.ui.viewmodelfactory.PokemonViewModelProviderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return ApiService.create()
    }

    @Singleton
    @Provides
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return PokemonDatabase(context)
    }

    @Singleton
    @Provides
    fun provideMainRepository(apiService: ApiService, pokemonDatabase: PokemonDatabase): MainRepository {
        return MainRepository(apiService, pokemonDatabase)
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(app: Application, mainRepository: MainRepository): ViewModelProvider.Factory {
        return PokemonViewModelProviderFactory(app, mainRepository)
    }
}