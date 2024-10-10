package org.ktb.chatbotbe.global.logging;

public record DiscordMessage(String content) {
    public static DiscordMessage createErrorDiscordMessage(String message) {
        return new DiscordMessage(message);
    }
}
