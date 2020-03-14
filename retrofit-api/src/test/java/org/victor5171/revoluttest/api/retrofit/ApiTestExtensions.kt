package org.victor5171.revoluttest.api.retrofit

import org.junit.Assume

const val API_BASE_URL_ENVIRONMENT_VARIABLE_KEY = "apiBaseUrl"

/**
 * Use this method when you want to test the api directly
 * It's a bad practice to put the API Url on the code, so this method makes sure that this test will
 * only be run if there's a base URL with the key [API_BASE_URL_ENVIRONMENT_VARIABLE_KEY] set in the
 * environment variables
 */
suspend inline fun testApiWithUrl(crossinline function: suspend (url: String) -> Unit) {
    val baseUrl = System.getenv(API_BASE_URL_ENVIRONMENT_VARIABLE_KEY)
    Assume.assumeFalse(baseUrl.isNullOrBlank())
    if (!baseUrl.isNullOrBlank()) {
        function(baseUrl)
    }
}
