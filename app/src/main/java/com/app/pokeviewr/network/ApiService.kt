package com.app.pokeviewr.network

import com.app.pokeviewr.model.PokemonDetailResponse
import com.app.pokeviewr.model.PokemonListResponse
import com.app.pokeviewr.model.PokemonSpeciesResponse
import com.app.pokeviewr.util.Constants.Companion.DOMAIN

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    companion object {
        fun create(): ApiService{
            val okHttpClientBuilder = OkHttpClient.Builder()

            var interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(interceptor)

            val authInterceptor = Interceptor { chain ->
                val originalRequest: Request = chain.request()
                val newRequest: Request = originalRequest.newBuilder()
                    .header("accept", "application/json")
                    .build()
                chain.proceed(newRequest)
            }
            okHttpClientBuilder.addInterceptor(authInterceptor)

            val client = okHttpClientBuilder
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(DOMAIN)
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("api/v2/pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int) : Response<PokemonListResponse>

    @GET("api/v2/pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") name: Int) : Response<PokemonDetailResponse>

    @GET("api/v2/pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") name: Int) : Response<PokemonSpeciesResponse>
}