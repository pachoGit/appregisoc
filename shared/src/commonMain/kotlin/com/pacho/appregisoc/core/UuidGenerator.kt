package com.pacho.appregisoc.core

import kotlin.random.Random

object UuidGenerator {
    fun generate(): String {
        val chars = "0123456789abcdef"
        val sections = intArrayOf(8, 4, 4, 4, 12)
        return sections.joinToString("-") { length ->
            (1..length).map { chars[Random.nextInt(chars.length)] }.joinToString("")
        }
    }
}
