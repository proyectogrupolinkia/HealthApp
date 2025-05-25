package com.tusalud.healthapp.domain.use_case

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tusalud.healthapp.domain.model.UserProfileData
import kotlinx.coroutines.tasks.await

import com.tusalud.healthapp.domain.repository.UserRepository

class GetUserProfileUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<UserProfileData> {
        return repository.getUserProfile()
    }
}