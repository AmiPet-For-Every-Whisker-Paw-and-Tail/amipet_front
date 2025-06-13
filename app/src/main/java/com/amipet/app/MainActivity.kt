package com.amipet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.amipet.app.di.AppModule
import com.amipet.app.test.BackendIntegrationTest
import com.amipet.app.ui.navigation.AmiPetNavigation
import com.amipet.app.ui.theme.AmiPetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Executar testes de integração com o backend em memória
        val sessionManager = AppModule.provideSessionManager(applicationContext)
        val apiClient = AppModule.provideApiClient(sessionManager)
        val backendTest = BackendIntegrationTest(apiClient, sessionManager)
        backendTest.runAllTests()

        setContent {
            AmiPetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AmiPetNavigation() // Ensure this includes ProfileScreen in the navigation graph
                }
            }
        }
    }
}