package com.amipet.app.local

import com.amipet.app.model.Animal
import com.amipet.app.model.Match
import com.amipet.app.model.MatchStatus
import com.amipet.app.model.Message
import com.amipet.app.model.User
import com.amipet.app.model.UserType

/**
 * Classe que fornece dados locais para o aplicativo quando o backend não está disponível
 * ou para testes offline.
 */
object LocalDataProvider {
    
    // Usuários de exemplo
    val currentUser = User(
        id = "user1",
        name = "Maria Silva",
        email = "maria@example.com",
        phone = "(11) 98765-4321",
        address = "Av. Paulista, 1000, São Paulo - SP",
        profileImageUrl = "https://randomuser.me/api/portraits/women/44.jpg",
        type = UserType.ADOPTER
    )
    
    val users = listOf(
        currentUser,
        User(
            id = "user2",
            name = "João Santos",
            email = "joao@example.com",
            phone = "(11) 91234-5678",
            address = "Rua Augusta, 500, São Paulo - SP",
            profileImageUrl = "https://randomuser.me/api/portraits/men/32.jpg",
            type = UserType.DONOR
        ),
        User(
            id = "user3",
            name = "Ana Oliveira",
            email = "ana@example.com",
            phone = "(11) 99876-5432",
            address = "Rua Oscar Freire, 200, São Paulo - SP",
            profileImageUrl = "https://randomuser.me/api/portraits/women/68.jpg",
            type = UserType.DONOR
        )
    )
    
    // Pets de exemplo
    val pets = listOf(
        Animal(
            id = "pet1",
            name = "Luna",
            species = "Cachorro",
            breed = "Labrador",
            age = "2 anos",
            gender = "Fêmea",
            size = "Grande",
            description = "Luna é uma labrador muito dócil e brincalhona. Adora crianças e é ótima companheira para famílias.",
            imageUrl = "https://images.unsplash.com/photo-1552053831-71594a27632d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8ZG9nfGVufDB8fDB8fHww&auto=format&fit=crop&w=800&q=60",
            ownerId = "user2",
            isAdopted = false
        ),
        Animal(
            id = "pet2",
            name = "Thor",
            species = "Cachorro",
            breed = "Golden Retriever",
            age = "1 ano",
            gender = "Macho",
            size = "Grande",
            description = "Thor é um golden retriever muito inteligente e amoroso. Já está treinado para comandos básicos.",
            imageUrl = "https://images.unsplash.com/photo-1561037404-61cd46aa615b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8ZG9nfGVufDB8fDB8fHww&auto=format&fit=crop&w=800&q=60",
            ownerId = "user2",
            isAdopted = false
        ),
        Animal(
            id = "pet3",
            name = "Mia",
            species = "Gato",
            breed = "Siamês",
            age = "3 anos",
            gender = "Fêmea",
            size = "Médio",
            description = "Mia é uma gata siamesa muito carinhosa e tranquila. Gosta de ambientes calmos e é ótima companheira.",
            imageUrl = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Y2F0fGVufDB8fDB8fHww&auto=format&fit=crop&w=800&q=60",
            ownerId = "user3",
            isAdopted = false
        ),
        Animal(
            id = "pet4",
            name = "Max",
            species = "Cachorro",
            breed = "Bulldog Francês",
            age = "2 anos",
            gender = "Macho",
            size = "Pequeno",
            description = "Max é um bulldog francês muito brincalhão e cheio de energia. Adora passear e brincar com bolas.",
            imageUrl = "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8ZG9nfGVufDB8fDB8fHww&auto=format&fit=crop&w=800&q=60",
            ownerId = "user3",
            isAdopted = false
        ),
        Animal(
            id = "pet5",
            name = "Nina",
            species = "Gato",
            breed = "Persa",
            age = "1 ano",
            gender = "Fêmea",
            size = "Pequeno",
            description = "Nina é uma gata persa muito dócil e tranquila. Gosta de carinho e de dormir em lugares confortáveis.",
            imageUrl = "https://images.unsplash.com/photo-1533738363-b7f9aef128ce?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8Y2F0fGVufDB8fDB8fHww&auto=format&fit=crop&w=800&q=60",
            ownerId = "user2",
            isAdopted = false
        )
    )

    // Matches de exemplo
    val matches = listOf(
        Match(
            id = "match1",
            userId = "user1",
            animalId = "pet1",
            timestamp = System.currentTimeMillis() - 86400000, // 1 dia atrás
            status = MatchStatus.APPROVED // Correct usage
        ),
        Match(
            id = "match2",
            userId = "user1",
            animalId = "pet3",
            timestamp = System.currentTimeMillis() - 172800000, // 2 dias atrás
            status = MatchStatus.PENDING // Correct usage
        )
    )
    
    // Mensagens de exemplo
    val messages = listOf(
        Message(
            id = "msg1",
            senderId = "user1",
            receiverId = "user2",
            content = "Olá, gostaria de saber mais sobre a Luna.",
            timestamp = System.currentTimeMillis() - 86400000, // 1 dia atrás
            matchId = "match1",
            isRead = true
        ),
        Message(
            id = "msg2",
            senderId = "user2",
            receiverId = "user1",
            content = "Olá! A Luna é muito dócil e está com todas as vacinas em dia. Quando você gostaria de conhecê-la?",
            timestamp = System.currentTimeMillis() - 82800000, // 23 horas atrás
            matchId = "match1",
            isRead = true
        ),
        Message(
            id = "msg3",
            senderId = "user1",
            receiverId = "user2",
            content = "Que ótimo! Poderia ser neste final de semana?",
            timestamp = System.currentTimeMillis() - 79200000, // 22 horas atrás
            matchId = "match1",
            isRead = false
        )
    )
}
