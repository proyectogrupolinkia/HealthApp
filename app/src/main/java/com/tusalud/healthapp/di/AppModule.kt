package com.tusalud.healthapp.di

import com.tusalud.healthapp.domain.use_case.GetProgressUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tusalud.healthapp.data.repository.ProgressRepositoryImpl
import com.tusalud.healthapp.domain.repository.ProgressRepository
import com.tusalud.healthapp.domain.repository.UserRepository
import com.tusalud.healthapp.data.repository.UserRepositoryImpl
import com.tusalud.healthapp.domain.use_case.DeleteUserAccountUseCase
import com.tusalud.healthapp.domain.use_case.GetUserProfileUseCase
import com.tusalud.healthapp.domain.use_case.GetWeightHistoryUseCase
import com.tusalud.healthapp.domain.use_case.LogoutUseCase
import com.tusalud.healthapp.domain.use_case.UpdateUserProfileUseCase
import com.tusalud.healthapp.domain.use_case.UpdateWeightUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideUserRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): UserRepository = UserRepositoryImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideProgressRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): ProgressRepository {
        return ProgressRepositoryImpl(auth, firestore)
    }

    @Provides
    fun provideUpdateWeightUseCase(
        progressRepository: ProgressRepository
    ): UpdateWeightUseCase = UpdateWeightUseCase(progressRepository)

    @Provides
    fun provideGetProgressUseCase(
        progressRepository: ProgressRepository
    ): GetProgressUseCase {
        return GetProgressUseCase(progressRepository)
    }


    @Provides
    fun provideGetWeightHistoryUseCase(
        progressRepository: ProgressRepository
    ): GetWeightHistoryUseCase {
        return GetWeightHistoryUseCase(progressRepository)
    }

    @Provides
    fun provideLogoutUseCase(
        userRepository: UserRepository
    ): LogoutUseCase = LogoutUseCase(userRepository)


    @Provides
    fun provideDeleteUserAccountUseCase(
        userRepository: UserRepository
    ): DeleteUserAccountUseCase {
        return DeleteUserAccountUseCase(userRepository)
    }



    @Provides
    fun provideUpdateUserProfileUseCase(
        userRepository: UserRepository
    ): UpdateUserProfileUseCase {
        return UpdateUserProfileUseCase(userRepository)
    }


    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(
        userRepository: UserRepository
    ): GetUserProfileUseCase {
        return GetUserProfileUseCase(userRepository)
    }

}