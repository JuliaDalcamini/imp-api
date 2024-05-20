package com.julia.imp.common.networking.error

import io.ktor.http.HttpStatusCode

class UnauthorizedError(message: String) : HttpError(HttpStatusCode.Unauthorized, message)