package com.elite.gemini.chatbot.data

/**
 * Represents a chat message in the conversation.
 * @param id Unique identifier for the message (auto-generated from timestamp)
 * @param content The text content of the message
 * @param isFromUser True if the message was sent by the user, false if from AI
 * @param timestamp Unix timestamp when the message was created
 * @param isLoading True if this is a placeholder message while waiting for AI response
 */
data class Message(
    val id: String = System.currentTimeMillis().toString(),
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false
)
