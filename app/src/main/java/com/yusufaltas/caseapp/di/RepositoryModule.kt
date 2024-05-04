package com.yusufaltas.caseapp.di

import com.yusufaltas.caseapp.data.repository.impl.CaseRepositoryImp
import com.yusufaltas.caseapp.data.repository.main.CaseRepository
import com.yusufaltas.caseapp.data.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun proviceCaseRepository(apiService: ApiService): CaseRepository {
        return CaseRepositoryImp(apiService)
    }

}