package com.elite.gemini.chatbot.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.elite.gemini.chatbot.data.Message
import com.elite.gemini.chatbot.ui.theme.aiMessageBackground
import com.elite.gemini.chatbot.ui.theme.userMessageGradientEnd
import com.elite.gemini.chatbot.ui.theme.userMessageGradientStart

@Composable
fun ChatMessageItem(
    message: Message,
    modifier: Modifier = Modifier
) {
    if (message.isLoading) {
        TypingIndicator()
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            if (message.isFromUser) {
                // User message - aligned to right with gradient
                UserMessageBubble(message.content)
            } else {
                // AI message - aligned to left
                AIMessageBubble(message.content)
            }
        }
    }
}

@Composable
private fun UserMessageBubble(content: String) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(userMessageGradientStart, userMessageGradientEnd)
                )
            )
            .padding(16.dp)
            .align(androidx.compose.ui.Alignment.CenterEnd)
    ) {
        Text(
            text = content,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.widthIn(max = 300.dp)
        )
    }
}

@Composable
private fun AIMessageBubble(content: String) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp))
            .background(aiMessageBackground)
            .padding(16.dp)
            .align(androidx.compose.ui.Alignment.CenterStart)
    ) {
        Text(
            text = content,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.widthIn(max = 300.dp)
        )
    }
}
