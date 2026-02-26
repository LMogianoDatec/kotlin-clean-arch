package com.datec.app.core.network

import java.io.IOException

class ApiException(
    val code: Int? = null,
    message: String,
) : IOException(message)
