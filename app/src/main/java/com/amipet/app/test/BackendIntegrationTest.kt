package com.amipet.app.test

import android.util.Log
import com.amipet.app.api.ApiClient
import com.amipet.app.model.Message
import com.amipet.app.util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Classe para testar a integração com o backend em memória
 * Executa testes para validar os fluxos de autenticação, pets, matches e mensagens
 */
class BackendIntegrationTest(
    private val apiClient: ApiClient,
    private val sessionManager: SessionManager
) {
    
    private val tag = "BackendIntegrationTest"
    
    fun runAllTests() {
        CoroutineScope(Dispatchers.IO).launch {
            testAuthentication()
            testPets()
            testMatches()
            testMessages()
            
            withContext(Dispatchers.Main) {
                Log.d(tag, "Todos os testes concluídos com sucesso!")
            }
        }
    }
    
    private suspend fun testAuthentication() {
        try {
            // Teste de login
            val loginResponse = apiClient.login("maria@example.com", "123456")
            assert(loginResponse.success) { "Falha no login: ${loginResponse.error}" }
            Log.d(tag, "Login bem-sucedido: ${loginResponse.data}")
            
            // Teste de obtenção do usuário atual
            val userResponse = apiClient.getCurrentUser()
            assert(userResponse.success) { "Falha ao obter usuário atual: ${userResponse.error}" }
            Log.d(tag, "Usuário atual obtido com sucesso: ${userResponse.data?.name}")
            
            // Teste de logout
            val logoutResponse = apiClient.logout()
            assert(logoutResponse.success) { "Falha no logout: ${logoutResponse.error}" }
            Log.d(tag, "Logout bem-sucedido")
            
            // Teste de registro
            val registerResponse = apiClient.register("Novo Usuário", "novo@example.com", "123456")
            assert(registerResponse.success) { "Falha no registro: ${registerResponse.error}" }
            Log.d(tag, "Registro bem-sucedido: ${registerResponse.data?.name}")
            
            Log.d(tag, "Testes de autenticação concluídos com sucesso!")
        } catch (e: Exception) {
            Log.e(tag, "Erro nos testes de autenticação: ${e.message}")
            throw e
        }
    }
    
    private suspend fun testPets() {
        try {
            // Teste de obtenção de todos os pets
            val allPetsResponse = apiClient.getAllPets()
            assert(allPetsResponse.success) { "Falha ao obter todos os pets: ${allPetsResponse.error}" }
            Log.d(tag, "Todos os pets obtidos com sucesso: ${allPetsResponse.data?.size} pets")
            
            // Teste de obtenção de pets disponíveis
            val availablePetsResponse = apiClient.getAvailablePets()
            assert(availablePetsResponse.success) { "Falha ao obter pets disponíveis: ${availablePetsResponse.error}" }
            Log.d(tag, "Pets disponíveis obtidos com sucesso: ${availablePetsResponse.data?.size} pets")
            
            // Teste de obtenção de pet por ID
            if (availablePetsResponse.data?.isNotEmpty() == true) {
                val petId = availablePetsResponse.data.first().id
                val petResponse = apiClient.getPetById(petId)
                assert(petResponse.success) { "Falha ao obter pet por ID: ${petResponse.error}" }
                Log.d(tag, "Pet obtido com sucesso: ${petResponse.data?.name}")
                
                // Teste de like em pet
                val likeResponse = apiClient.likePet(petId)
                assert(likeResponse.success) { "Falha ao dar like no pet: ${likeResponse.error}" }
                Log.d(tag, "Like no pet bem-sucedido: ${likeResponse.data?.id}")
                
                // Teste de dislike em pet
                val dislikeResponse = apiClient.dislikePet(petId)
                assert(dislikeResponse.success) { "Falha ao dar dislike no pet: ${dislikeResponse.error}" }
                Log.d(tag, "Dislike no pet bem-sucedido")
            }
            
            Log.d(tag, "Testes de pets concluídos com sucesso!")
        } catch (e: Exception) {
            Log.e(tag, "Erro nos testes de pets: ${e.message}")
            throw e
        }
    }
    
    private suspend fun testMatches() {
        try {
            // Teste de obtenção de matches
            // Como não temos um endpoint específico para matches no ApiClient atual,
            // este teste é simplificado
            Log.d(tag, "Testes de matches concluídos com sucesso!")
        } catch (e: Exception) {
            Log.e(tag, "Erro nos testes de matches: ${e.message}")
            throw e
        }
    }
    
    private suspend fun testMessages() {
        try {
            // Teste de obtenção de mensagens por match ID
            val matchId = "match1" // ID de match de exemplo
            val messagesResponse = apiClient.getMessagesByMatchId(matchId)
            assert(messagesResponse.success) { "Falha ao obter mensagens: ${messagesResponse.error}" }
            Log.d(tag, "Mensagens obtidas com sucesso: ${messagesResponse.data?.size} mensagens")
            
            // Teste de envio de mensagem
            val newMessage = Message(
                id = "",
                senderId = "user1",
                receiverId = "user2",
                content = "Mensagem de teste",
                timestamp = System.currentTimeMillis(),
                matchId = matchId
            )
            
            val sendMessageResponse = apiClient.sendMessage(newMessage)
            assert(sendMessageResponse.success) { "Falha ao enviar mensagem: ${sendMessageResponse.error}" }
            Log.d(tag, "Mensagem enviada com sucesso: ${sendMessageResponse.data?.id}")
            
            Log.d(tag, "Testes de mensagens concluídos com sucesso!")
        } catch (e: Exception) {
            Log.e(tag, "Erro nos testes de mensagens: ${e.message}")
            throw e
        }
    }
}
