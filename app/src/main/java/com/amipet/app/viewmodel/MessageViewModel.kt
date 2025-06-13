package com.amipet.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amipet.app.model.Message
import com.amipet.app.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageViewModel(private val messageRepository: MessageRepository) : ViewModel() {
    
    private val _messagesState = MutableStateFlow<MessagesState>(MessagesState.Loading)
    val messagesState: StateFlow<MessagesState> = _messagesState
    
    private val _sendMessageState = MutableStateFlow<SendMessageState>(SendMessageState.Idle)
    val sendMessageState: StateFlow<SendMessageState> = _sendMessageState
    
    fun getMessagesByMatchId(matchId: String) {
        _messagesState.value = MessagesState.Loading
        
        viewModelScope.launch {
            try {
                val response = messageRepository.getMessagesByMatchId(matchId)
                if (response.success && response.data != null) {
                    if (response.data.isEmpty()) {
                        _messagesState.value = MessagesState.Empty
                    } else {
                        _messagesState.value = MessagesState.Success(response.data)
                    }
                } else {
                    _messagesState.value = MessagesState.Error(response.error ?: "Falha ao carregar mensagens")
                }
            } catch (e: Exception) {
                _messagesState.value = MessagesState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
    
    fun sendMessage(message: Message) {
        _sendMessageState.value = SendMessageState.Loading
        
        viewModelScope.launch {
            try {
                val response = messageRepository.sendMessage(message)
                if (response.success && response.data != null) {
                    _sendMessageState.value = SendMessageState.Success(response.data)
                    // Recarregar mensagens após envio bem-sucedido
                    message.matchId?.let { getMessagesByMatchId(it) }
                } else {
                    _sendMessageState.value = SendMessageState.Error(response.error ?: "Falha ao enviar mensagem")
                }
            } catch (e: Exception) {
                _sendMessageState.value = SendMessageState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
    
    fun markAsRead(id: String) {
        viewModelScope.launch {
            try {
                messageRepository.markAsRead(id)
            } catch (e: Exception) {
                // Tratar erro se necessário
            }
        }
    }
    
    fun resetSendMessageState() {
        _sendMessageState.value = SendMessageState.Idle
    }
    
    sealed class MessagesState {
        object Loading : MessagesState()
        object Empty : MessagesState()
        data class Success(val messages: List<Message>) : MessagesState()
        data class Error(val message: String) : MessagesState()
    }
    
    sealed class SendMessageState {
        object Idle : SendMessageState()
        object Loading : SendMessageState()
        data class Success(val message: Message) : SendMessageState()
        data class Error(val message: String) : SendMessageState()
    }
}
