package com.elite.gemini.chatbot.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elite.gemini.chatbot.data.Message
import com.elite.gemini.chatbot.domain.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ChatUiState {
    object Initial : ChatUiState()
    object Loading : ChatUiState()
    data class Success(val messages: List<Message>) : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}

class ChatViewModel(application: Application) : ViewModel() {
    
    private val repository = ChatRepository()
    private val sharedPreferences = application.getSharedPreferences("gemini_chat_prefs", Application.MODE_PRIVATE)
    
    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Initial)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()
    
    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()
    
    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()
    
    init {
        loadApiKey()
    }
    
    fun setApiKey(key: String) {
        _apiKey.value = key
        saveApiKey(key)
    }
    
    fun sendMessage(message: String) {
        if (message.isBlank()) return
        if (_apiKey.value.isBlank()) {
            _uiState.value = ChatUiState.Error("Please set your Gemini API key first")
            return
        }
        
        val userMessage = Message(
            content = message,
            isFromUser = true
        )
        
        val loadingMessage = Message(
            content = "",
            isFromUser = false,
            isLoading = true
        )
        
        _messages.value = _messages.value + userMessage + loadingMessage
        _isTyping.value = true
        
        viewModelScope.launch {
            _uiState.value = ChatUiState.Loading
            
            repository.sendMessage(message, _apiKey.value)
                .onSuccess { response ->
                    val updatedMessages = _messages.value.dropLast(1) + loadingMessage.copy(
                        content = response,
                        isLoading = false
                    )
                    _messages.value = updatedMessages
                    _uiState.value = ChatUiState.Success(updatedMessages)
                }
                .onFailure { error ->
                    val errorMessage = Message(
                        content = "Error: ${error.message}",
                        isFromUser = false,
                        isLoading = false
                    )
                    _messages.value = _messages.value.dropLast(1) + errorMessage
                    _uiState.value = ChatUiState.Error(error.message ?: "Unknown error")
                }
            
            _isTyping.value = false
        }
    }
    
    fun clearChat() {
        _messages.value = emptyList()
        _uiState.value = ChatUiState.Initial
    }
    
    fun dismissError() {
        if (_uiState.value is ChatUiState.Error) {
            _uiState.value = ChatUiState.Success(_messages.value)
        }
    }
    
    private fun saveApiKey(key: String) {
        sharedPreferences.edit().putString("api_key", key).apply()
    }
    
    private fun loadApiKey() {
        _apiKey.value = sharedPreferences.getString("api_key", "") ?: ""
    }
}
