package com.yusufaltas.caseapp.data.repository.main

import com.yusufaltas.caseapp.model.GetSessionModel
import com.yusufaltas.caseapp.model.GetSessionResultModel
import retrofit2.Response

interface CaseRepository {

    suspend fun getSession(session: GetSessionModel): Response<GetSessionResultModel>

}