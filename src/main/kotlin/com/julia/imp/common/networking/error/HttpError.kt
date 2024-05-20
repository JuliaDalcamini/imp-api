package com.julia.imp.common.networking.error

import io.ktor.http.HttpStatusCode

abstract class HttpError(val statusCode: HttpStatusCode, override val message: String) : Error(message)