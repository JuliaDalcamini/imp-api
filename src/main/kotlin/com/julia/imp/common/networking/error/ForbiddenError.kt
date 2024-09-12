package com.julia.imp.common.networking.error

import io.ktor.http.HttpStatusCode

class ForbiddenError(message: String) : HttpError(HttpStatusCode.Forbidden, message)