package org.victor5171.revoluttest.api.retrofit

import org.junit.Test

class ApiBuilderTest {
    @Test
    fun `When I try to create an AndroidRetrofitService, it should work`() {
        val retrofitBuilder = ApiBuilder("http://realurl.com/api/")
        retrofitBuilder.androidApi
    }
}
