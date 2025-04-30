package com.tusalud.healthapp.domain.repository

import com.tusalud.healthapp.domain.model.Progress

interface ProgressRepository {
    suspend fun getProgress(): Progress
}