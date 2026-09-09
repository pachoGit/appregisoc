package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.data.session.SessionManager

class LogoutUseCase(
    private val sessionManager: SessionManager
) {
    operator fun invoke() {
        sessionManager.clearSession()
    }
}
