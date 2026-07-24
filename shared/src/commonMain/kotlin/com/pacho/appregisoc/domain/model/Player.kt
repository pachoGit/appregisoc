package com.pacho.appregisoc.domain.model

data class Player(
    val id: String,
    val firstNames: String,
    val lastNames: String,
    val dni: String,
    val birthDate: Long,
    val age: Int,
    val photoUrl: String? = null,
    val dniFrontPhotoUrl: String? = null,
    val dniBackPhotoUrl: String? = null
)
