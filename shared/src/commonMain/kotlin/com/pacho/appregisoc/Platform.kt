package com.pacho.appregisoc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform