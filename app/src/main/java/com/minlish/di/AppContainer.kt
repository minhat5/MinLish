package com.minlish.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.minlish.data.remote.FirebaseAuthService
import com.minlish.data.repository.AuthRepositoryImpl
import com.minlish.domain.repository.AuthRepository
import com.minlish.domain.usecase.GetCurrentUserUseCase
import com.minlish.domain.usecase.IsLoggedInUseCase
import com.minlish.domain.usecase.LoginUseCase
import com.minlish.domain.usecase.LogoutUseCase
import com.minlish.domain.usecase.ObserveAuthStateUseCase
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

    // Repositories
    private val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(firebaseAuthService)
    }

    // Use Cases
    val registerUseCase: RegisterUseCase by lazy { RegisterUseCase(authRepository) }
    val loginUseCase: LoginUseCase by lazy { LoginUseCase(authRepository) }
    val logoutUseCase: LogoutUseCase by lazy { LogoutUseCase(authRepository) }
    val getCurrentUserUseCase: GetCurrentUserUseCase by lazy { GetCurrentUserUseCase(authRepository) }
    val isLoggedInUseCase: IsLoggedInUseCase by lazy { IsLoggedInUseCase(authRepository) }
    val observeAuthStateUseCase: ObserveAuthStateUseCase by lazy { ObserveAuthStateUseCase(authRepository) }
    val resetPasswordUseCase: ResetPasswordUseCase by lazy { ResetPasswordUseCase(authRepository) }
    val updateUserProfileUseCase: UpdateUserProfileUseCase by lazy { UpdateUserProfileUseCase(authRepository) }

    fun initialize(appContext: Context) {
        context = appContext
    }
}

