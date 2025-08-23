package me.delyfss.medved.base.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.delyfss.medved.base.api.ModelApi
import me.delyfss.medved.base.entity.Entity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

typealias ModelCallback = (ResponseModel) -> Unit

object ModelService {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val entityAdapter = moshi.adapter(Entity::class.java)

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://inference:5003/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val api = retrofit.create(ModelApi::class.java)

    fun test(entity: Entity, callback: ModelCallback) {
        val json = entityAdapter.toJson(entity)
        val body = json.toRequestBody("application/json".toMediaTypeOrNull())

        api.test("test", body).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                response.body()?.let(callback)
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
