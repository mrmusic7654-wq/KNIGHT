package com.elite.gemini.chatbot.domain

import com.elite.gemini.chatbot.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository {
    
    private val apiService = RetrofitClient.geminiApiService
    
    suspend fun sendMessage(message: String, apiKey: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(text = message))
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.7f,
                    topK = 40,
                    topP = 0.95f,
                    maxOutputTokens = 1024
                )
            )
            
            val response = apiService.generateContent(apiKey, request)
            
            if (response.error != null) {
                Result.failure(Exception("API Error: ${response.error.message}"))
            } else {
                val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "No response from AI"
                Result.success(reply)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun sendChatMessage(messages: List<Message>, apiKey: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val contentList = messages.map { msg ->
                Content(
                    parts = listOf(Part(text = msg.content)),
                    role = if (msg.isFromUser) "user" else "model"
                )
            }
            
            val request = GeminiRequest(
                contents = contentList,
                generationConfig = GenerationConfig(
                    temperature = 0.7f,
                    topK = 40,
                    topP = 0.95f,
                    maxOutputTokens = 1024
                )
            )
            
            val response = apiService.generateContent(apiKey, request)
            
            if (response.error != null) {
                Result.failure(Exception("API Error: ${response.error.message}"))
            } else {
                val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "No response from AI"
                Result.success(reply)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
