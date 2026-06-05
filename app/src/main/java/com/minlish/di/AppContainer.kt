package com.minlish.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.minlish.data.remote.FirebaseAuthService
import com.minlish.data.remote.FirebaseDeckService
import com.minlish.data.remote.FirebaseProfileService
import com.minlish.data.repository.AnalyticsRepositoryImpl
import com.minlish.data.repository.AuthRepositoryImpl
import com.minlish.data.repository.FlashcardRepositoryImpl
import com.minlish.data.repository.ProfileRepositoryImpl
import com.minlish.domain.repository.AnalyticsRepository
import com.minlish.domain.repository.AuthRepository
import com.minlish.domain.repository.FlashcardRepository
import com.minlish.domain.repository.ProfileRepository
import com.minlish.domain.usecase.GetCurrentUserUseCase
import com.minlish.domain.usecase.GetProfileStatsUseCase
import com.minlish.data.repository.DashboardRepositoryImpl
import com.minlish.domain.repository.DashboardRepository
import com.minlish.domain.usecase.GetAnalyticsMetricsUseCase
import com.minlish.domain.usecase.GetDashboardMetricsUseCase
import com.minlish.domain.usecase.IsLoggedInUseCase
import com.minlish.domain.usecase.LoginUseCase
import com.minlish.domain.usecase.LogoutUseCase
import com.minlish.domain.usecase.RegisterUseCase
import com.minlish.domain.usecase.ResetPasswordUseCase
import com.minlish.domain.usecase.UpdateUserProfileUseCase

/**
 * Service Locator for Dependency Injection
 * This is a simple DI container to manage dependencies
 */
object AppContainer {
    private lateinit var context: Context

    // Firebase instances (singletons)
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseFirestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // Services
    private val firebaseAuthService: FirebaseAuthService by lazy {
        FirebaseAuthService(firebaseAuth, firebaseFirestore)
    }
    private val firebaseProfileService: FirebaseProfileService by lazy {
        FirebaseProfileService(firebaseFirestore)
    }
    val firebaseDeckService: FirebaseDeckService by lazy {
        FirebaseDeckService(firebaseFirestore)
    }

    // Repositories
    private val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(firebaseAuthService)
    }
    private val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(firebaseProfileService)
    }

    private val dashboardRepository: DashboardRepository by lazy {
        DashboardRepositoryImpl(firebaseFirestore)
    }
    val flashcardRepository: FlashcardRepository by lazy {
        FlashcardRepositoryImpl(firebaseFirestore)
    }

    private val analyticsRepository: AnalyticsRepository by lazy {
        AnalyticsRepositoryImpl(firebaseFirestore)
    }

    // Use Cases
    val registerUseCase: RegisterUseCase by lazy { RegisterUseCase(authRepository) }
    val loginUseCase: LoginUseCase by lazy { LoginUseCase(authRepository) }
    val logoutUseCase: LogoutUseCase by lazy { LogoutUseCase(authRepository) }
    val getCurrentUserUseCase: GetCurrentUserUseCase by lazy { GetCurrentUserUseCase(authRepository) }
    val isLoggedInUseCase: IsLoggedInUseCase by lazy { IsLoggedInUseCase(authRepository) }
    val resetPasswordUseCase: ResetPasswordUseCase by lazy { ResetPasswordUseCase(authRepository) }
    val updateUserProfileUseCase: UpdateUserProfileUseCase by lazy { UpdateUserProfileUseCase(authRepository) }
    val getProfileStatsUseCase: GetProfileStatsUseCase by lazy { GetProfileStatsUseCase(profileRepository) }
    val getDashboardMetricsUseCase: GetDashboardMetricsUseCase by lazy { GetDashboardMetricsUseCase(dashboardRepository) }
    val getAnalyticsMetricsUseCase: GetAnalyticsMetricsUseCase by lazy { GetAnalyticsMetricsUseCase(analyticsRepository) }

    fun initialize(appContext: Context) {
        context = appContext
    }

    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}

