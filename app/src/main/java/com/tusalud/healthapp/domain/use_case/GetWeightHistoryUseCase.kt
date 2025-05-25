package com.tusalud.healthapp.domain.use_case

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tusalud.healthapp.domain.repository.ProgressRepository
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetWeightHistoryUseCase @Inject constructor(
    private val repository: ProgressRepository
) {
    suspend operator fun invoke(): Result<Pair<List<Float>, List<Pair<Float, String>>>> {
        return repository.getWeightHistory()
    }
}
