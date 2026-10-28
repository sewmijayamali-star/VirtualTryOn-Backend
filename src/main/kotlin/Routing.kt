package com.ZeRo

import com.ZeRo.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.ZeRo.routes.digitalModelRoutes

fun Application.configureRouting() {
    routing {
        userRoutes()
        digitalModelRoutes()
    }
}