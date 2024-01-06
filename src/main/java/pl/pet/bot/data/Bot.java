package pl.pet.bot.data;

import java.io.Serializable;
import lombok.Builder;

/**
 * The Bot class represents a bot with its unique identifier, state, and location.
 *
 * @param state bot state.
 * @param botId bot id.
 * @param location bot location.
 */
@Builder
public record Bot(
    String botId,
    BotState state,
    BotLocation location
) implements Serializable {
}

