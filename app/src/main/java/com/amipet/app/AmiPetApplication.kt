package com.amipet.app

import android.app.Application
import com.amipet.app.di.AppModule
import com.amipet.app.repository.AuthRepository
import com.amipet.app.repository.MessageRepository
import com.amipet.app.repository.PetRepository
import com.amipet.app.repository.UserRepository

class AmiPetApplication : Application() {
    
    // Repositórios
    lateinit var authRepository: AuthRepository
    lateinit var petRepository: PetRepository
    lateinit var userRepository: UserRepository
    lateinit var messageRepository: MessageRepository
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar repositórios usando o módulo de injeção de dependência
        authRepository = AppModule.provideAuthRepository(applicationContext)
        petRepository = AppModule.providePetRepository(applicationContext)
        userRepository = AppModule.provideUserRepository(applicationContext)
        messageRepository = AppModule.provideMessageRepository(applicationContext)
    }
}
