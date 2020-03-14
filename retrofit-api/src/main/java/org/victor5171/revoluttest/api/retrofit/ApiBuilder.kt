package org.victor5171.revoluttest.api.retrofit

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

internal class ApiBuilder(baseUrl: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val androidApi = RetrofitAndroidApi(retrofit.create())
}
