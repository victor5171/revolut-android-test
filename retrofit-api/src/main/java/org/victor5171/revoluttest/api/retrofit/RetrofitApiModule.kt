package org.victor5171.revoluttest.api.retrofit

import dagger.Module
import dagger.Provides
import org.victor5171.revoluttest.api.AndroidApi
import org.victor5171.revoluttest.api.retrofit.di.BaseUrl
import javax.inject.Singleton

@Module
class RetrofitApiModule {

    private var apiBuilder: ApiBuilder? = null

    /**
     * Unfortunately I have to call this method every time, because this is the only way I can build
     * daos, requiring the bound BaseUrl parameter
     */
    private fun buildApiBuilder(baseUrl: String): ApiBuilder {
        apiBuilder?.let { return it }

        return ApiBuilder(baseUrl).also { apiBuilder = it }
    }

    @Singleton
    @Provides
    fun providesAndroidApi(@BaseUrl baseUrl: String): AndroidApi {
        return buildApiBuilder(baseUrl).androidApi
    }
}
