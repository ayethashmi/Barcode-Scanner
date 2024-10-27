package com.devappspros.barcodescanner.data.network

import com.devappspros.barcodescanner.common.exceptions.WSException
import okhttp3.Interceptor
import okhttp3.Response

class WSApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val response: Response

        try {
            response = chain.proceed(requestBuilder.build())
        } catch (e: Exception) {
            /*throw when(e){
                is ExecutionException -> NoInternetConnectionException()
                is IOException -> NoInternetConnectionException()
                else -> NetworkErrorException()
            }*/
            throw e
        }

        if(!response.isSuccessful){
            throw com.devappspros.barcodescanner.common.exceptions.WSException(response)
        }

        return response
    }
}