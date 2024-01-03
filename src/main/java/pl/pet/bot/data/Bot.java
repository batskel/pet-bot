package pl.pet.bot.data;

import java.io.Serializable;
import lombok.Builder;

/**
 * Represents a Bot.
 *
 * @param botId bot id.
 * @param state bot state.
 */
@Builder
public record Bot(String botId, BotState state) implements Serializable {
}

