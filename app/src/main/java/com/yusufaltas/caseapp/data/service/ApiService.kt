package com.yusufaltas.caseapp.data.service

import com.yusufaltas.caseapp.model.GetSessionModel
import com.yusufaltas.caseapp.model.GetSessionResultModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    //Get Token
    @POST("client/getsession")
    suspend fun getSession(
        @Body sessionModel: GetSessionModel
    ): Response<GetSessionResultModel>

}