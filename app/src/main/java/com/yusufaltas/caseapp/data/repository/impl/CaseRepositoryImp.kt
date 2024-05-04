package com.yusufaltas.caseapp.data.repository.impl

import com.yusufaltas.caseapp.data.repository.main.CaseRepository
import com.yusufaltas.caseapp.data.service.ApiService
import com.yusufaltas.caseapp.model.GetSessionModel
import com.yusufaltas.caseapp.model.GetSessionResultModel
import retrofit2.Response
import javax.inject.Inject

class CaseRepositoryImp @Inject constructor(private val apiService: ApiService) : CaseRepository {

    override suspend fun getSession(session: GetSessionModel): Response<GetSessionResultModel> {
        return apiService.getSession(session)
    }

}