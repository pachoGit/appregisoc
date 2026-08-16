package com.pacho.appregisoc.domain.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.MatchDateResponse

interface MatchDateRepository {
    suspend fun getByEventAndClub(eventId: Long, clubId: Long): Result<List<MatchDateResponse>>
}
