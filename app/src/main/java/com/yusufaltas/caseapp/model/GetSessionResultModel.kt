package com.yusufaltas.caseapp.model

import com.google.gson.annotations.SerializedName

data class GetSessionResultModel(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("data")
    val data: Any? = Any(),
    @SerializedName("message")
    val message: Any? = Any(),
    @SerializedName("user-message")
    val userMessage: Any? = Any(),
    @SerializedName("api-request-id")
    val apiRequestId: Any? = Any(),
    val controller: String = "",
    @SerializedName("client-request-id")
    val clientRequestId: Any? = Any(),
    @SerializedName("web-correlation-id")
    val webCorrelationId: Any? = Any(),
    @SerializedName("correlation-id")
    val correlationId: String = "",
    @SerializedName("parameters")
    val parameters: Any? = Any()
)
