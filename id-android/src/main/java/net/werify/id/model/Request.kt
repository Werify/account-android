package net.werify.id.model

import okhttp3.RequestBody

interface Request {
    fun toRequestBody(): RequestBody
}