package com.devappspros.barcodescanner.common.exceptions

import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection

class WSException(val code: Int, val type: com.devappspros.barcodescanner.common.exceptions.WSException.Type, message: String?): IOException(message) {
    constructor(response: Response): this(response.code, response.type(), response.message)

    enum class Type {
        NOT_FOUND,
        UNAUTHORISED,
        UPDATE_REQUIRED,
        FORBIDDEN,
        OTHER
    }

    override fun toString(): String {
        return "Error type: $type (Error $code)\n${super.toString()}"
    }

    companion object {
        private fun Response.type(): com.devappspros.barcodescanner.common.exceptions.WSException.Type {
            return when(code){
                HttpURLConnection.HTTP_NOT_FOUND -> com.devappspros.barcodescanner.common.exceptions.WSException.Type.NOT_FOUND
                HttpURLConnection.HTTP_UNAUTHORIZED -> com.devappspros.barcodescanner.common.exceptions.WSException.Type.UNAUTHORISED
                426 -> com.devappspros.barcodescanner.common.exceptions.WSException.Type.UPDATE_REQUIRED
                HttpURLConnection.HTTP_FORBIDDEN -> com.devappspros.barcodescanner.common.exceptions.WSException.Type.FORBIDDEN
                else -> com.devappspros.barcodescanner.common.exceptions.WSException.Type.OTHER
            }
        }
    }
}