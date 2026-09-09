package com.pacho.appregisoc.domain.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.LineupResponse
import com.pacho.appregisoc.data.dto.SetLineupRequest

interface LineupRepository {
    suspend fun getByMatch(matchId: Long, clubId: Long): Result<LineupResponse?>
    suspend fun create(request: SetLineupRequest): Result<LineupResponse>
    suspend fun update(id: Long, request: SetLineupRequest): Result<LineupResponse>
    suspend fun close(id: Long): Result<Unit>
}
