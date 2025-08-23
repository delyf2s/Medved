package me.delyfss.medved.base.api

import me.delyfss.medved.base.model.ResponseModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ModelApi {
    @POST
    @Headers("Content-Type: application/json")
    fun test(
        @Url url: String,
        @Body body: RequestBody,
    ): Call<ResponseModel>
}
