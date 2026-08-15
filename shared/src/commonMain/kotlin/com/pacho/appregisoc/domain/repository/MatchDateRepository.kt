package com.pacho.appregisoc.domain.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.MatchDateResponse

interface MatchDateRepository {
    suspend fun getByEvent(eventId: Long): Result<List<MatchDateResponse>>
    suspend fun getById(id: Long): Result<MatchDateResponse?>
}
