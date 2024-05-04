package com.yusufaltas.caseapp.di

import android.content.Context
import com.yusufaltas.caseapp.data.service.utils.SharedPreferencesManager
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
    fun provideDataStoreManager(@ApplicationContext context: Context) = SharedPreferencesManager(context = context)
}