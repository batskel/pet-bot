package pl.pet.bot.emulation;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import pl.pet.bot.data.Bot;
import pl.pet.bot.data.BotLocation;
import pl.pet.bot.data.BotState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The BotEmulation class represents a service for bot emulation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BotEmulation {

  /**
   * The {@code faker} variable is an instance of the {@code Faker} class.
   * It is used to generate fake data for testing and development purposes.
   */
  private static final Faker FAKER = new Faker();

  /**
   * An instance of the {@code ReactiveRedisTemplate} class that provides a high-level access to Redis operations
   * with reactive support. It is used to interact with a Redis server for storing and retrieving data of type {@code Bot}
   * identified by a {@code String} key.
   */
  private final ReactiveRedisTemplate<String, Bot> redisTemplate;

  /**
   * The {@code botCount} variable represents the number of bots in the system.
   * It is initialized from the configuration property "emulation.bot.count".
   */
  @Value("${emulation.bot.count}")
  private Integer botCount;

  /**
   * Represents the interval for emulation.
   */
  @Value("${emulation.interval}")
  private Integer interval;

  /**
   * The initiate bot method.
   */
  @PostConstruct
  private void initiate() {
    Mono.empty()
        .then(this.deleteAll())
        .then(this.createBotSteam())
        .then(this.emulate())
        .subscribe();
  }

  /**
   * Deletes all keys from the Redis database.
   *
   * @return A Mono that completes when the deletion is finished.
   */
  private Mono<Void> deleteAll() {
    return this.redisTemplate
        .scan()
        .collectList()
        .filter(ids -> !ids.isEmpty())
        .flatMap(keyList -> this.redisTemplate.delete(keyList.toArray(new String[0])))
        .then();
  }

  /**
   * Creates a stream of Bot instances.
   *
   * @return a Flux<Bot> representing the stream of Bot instances.
   */
  private Mono<Void> createBotSteam() {
    return Flux.<Bot>create(botFluxSink -> {
          for (int i = 0; i < botCount; i++) {
            final String botId = String.format("BOT-%04d", FAKER.number().numberBetween(1, 9999));
            botFluxSink.next(this.emulateBotStateById(botId));
            log.info("Created and published [bot_id: {}]", botId);
          }
          botFluxSink.complete();
        })
        .flatMap(bot -> this.redisTemplate.opsForValue().set(bot.botId(), bot))
        .then();
  }

  /**
   * Emulates the state of a bot identified by its ID.
   *
   * @param botId the ID of the bot
   * @return a Bot object representing the state of the bot
   */
  private Bot emulateBotStateById(final String botId) {
    final BotState botState = FAKER.options().option(BotState.class);
    final byte x = (byte) FAKER.number().numberBetween(Byte.MIN_VALUE, Byte.MAX_VALUE);
    final byte y = (byte) FAKER.number().numberBetween(Byte.MIN_VALUE, Byte.MAX_VALUE);
    return Bot.builder()
        .botId(botId)
        .state(botState)
        .location(BotLocation.builder().x(x).y(y).build())
        .build();
  }

  /**
   * Emulates the state of a bot identified.
   *
   * @return a Mono<Void> representing the completion of the emulation
   */
  private Mono<Void> emulate() {
    return Flux.interval(Duration.ofMillis(this.interval))
        .doFirst(() -> log.info("Start bot emulation"))
        .flatMap(aLong -> this.redisTemplate.scan().flatMap(id -> this.redisTemplate.opsForValue().get(id)))
        .map(bot -> this.emulateBotStateById(bot.botId()))
        .collectMap(Bot::botId, bot -> bot)
        .flatMap(botMap -> this.redisTemplate.opsForValue().multiSet(botMap))
        .then();
  }

}
