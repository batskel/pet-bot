package pl.pet.bot.data;

import lombok.Builder;

/**
 * BotLocation represents the location of a bot in a two-dimensional grid.
 *
 * @param x x location
 * @param y y location
 */
@Builder
public record BotLocation(byte x, byte y) {
}
