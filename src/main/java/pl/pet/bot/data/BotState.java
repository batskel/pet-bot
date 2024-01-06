package pl.pet.bot.data;

/**
 * Represents the state of a bot.
 */
public enum BotState {

  /**
   * The bot is not doing anything and is waiting for commands.
   */
  BOT_IDLE,

  /**
   * The bot is active
   */
  BOT_ACTIVE,

  /**
   * The bot is currently handling a request/task.
   */
  BOT_BOOTING_UP,

  /**
   * The bot is in the process of starting up.
   */
  BOT_SUSPENDED,

  /**
   * The bot is in the process of shutting down.
   */
  BOT_SHUTTING_DOWN,

  /**
   * Represents the state of a bot when it is down.
   */
  BOT_DOWN,
}
