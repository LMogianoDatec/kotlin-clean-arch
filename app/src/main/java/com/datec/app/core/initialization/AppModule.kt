package com.datec.app.core.initialization

import com.datec.app.core.initialization.EnvConfig
import com.datec.app.module.domain.repositories.BookRepository
import com.datec.app.module.data.repositories.BookRepositoryImpl
import com.datec.app.core.network.ApiClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton
import javax.inject.Named
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindBookRepository(
        impl: BookRepositoryImpl
    ): BookRepository

    companion object {
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        @Provides
        @Singleton
        @Named("books")
        fun provideBookApiClient(
            @ApplicationContext appContext: Context,
            okHttpClient: OkHttpClient
        ): ApiClient {

            return ApiClient(
                okHttpClient = okHttpClient,
                baseUrl = EnvConfig.API_BASE_URL
            )
        }
    }
}