package net.werify.official.model

import okhttp3.RequestBody

interface Request {
    fun toRequestBody(): RequestBody
}