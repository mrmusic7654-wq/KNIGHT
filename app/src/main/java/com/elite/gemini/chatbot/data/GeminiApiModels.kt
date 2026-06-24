package com.elite.gemini.chatbot.data

import com.google.gson.annotations.SerializedName

/**
 * Request model for Gemini API content generation.
 * @param contents List of content messages (conversation history)
 * @param generationConfig Optional configuration for response generation
 */
data class GeminiRequest(
    @SerializedName("contents")
    val contents: List<Content>,
    @SerializedName("generationConfig")
    val generationConfig: GenerationConfig? = null
)

/**
 * Content model representing a single message in the conversation.
 * @param parts List of text parts in the message
 * @param role The role of the sender ("user" or "model")
 */
data class Content(
    @SerializedName("parts")
    val parts: List<Part>,
    @SerializedName("role")
    val role: String = "user"
)

/**
 * Part model containing the actual text content of a message.
 */
data class Part(
    @SerializedName("text")
    val text: String
)

/**
 * Configuration options for controlling the AI's response generation.
 * @param temperature Controls randomness (0.0-1.0, higher = more creative)
 * @param topK Nucleus sampling parameter
 * @param topP Cumulative probability threshold for token selection
 * @param maxOutputTokens Maximum number of tokens in the response
 */
data class GenerationConfig(
    @SerializedName("temperature")
    val temperature: Float = 0.7f,
    @SerializedName("topK")
    val topK: Int = 40,
    @SerializedName("topP")
    val topP: Float = 0.95f,
    @SerializedName("maxOutputTokens")
    val maxOutputTokens: Int = 1024
)

/**
 * Response model from Gemini API.
 * @param candidates List of generated responses (usually one)
 * @param error Error information if the request failed
 */
data class GeminiResponse(
    @SerializedName("candidates")
    val candidates: List<Candidate>?,
    @SerializedName("error")
    val error: ErrorBody?
)

/**
 * Candidate response containing the AI's generated content.
 * @param content The response content
 * @param finishReason Reason why generation stopped (e.g., "STOP", "MAX_TOKENS")
 */
data class Candidate(
    @SerializedName("content")
    val content: ResponseContent,
    @SerializedName("finishReason")
    val finishReason: String?
)

/**
 * Content of the AI's response.
 * @param parts List of text parts in the response
 * @param role Always "model" for AI responses
 */
data class ResponseContent(
    @SerializedName("parts")
    val parts: List<ResponsePart>,
    @SerializedName("role")
    val role: String
)

/**
 * Individual text part in the AI's response.
 */
data class ResponsePart(
    @SerializedName("text")
    val text: String
)

/**
 * Error response from the API.
 * @param code HTTP status code
 * @param message Error description
 */
data class ErrorBody(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
