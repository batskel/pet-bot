package pl.pet.bot.controllers;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pet.bot.data.Bot;
import reactor.core.publisher.Flux;

/**
 * The BotController class is responsible for handling HTTP requests related to bots.
 */
@Slf4j
@RestController
@RequestMapping("/bots")
@RequiredArgsConstructor
public class BotController {

  /**
   * An instance of the {@code ReactiveRedisTemplate} class that provides a high-level access to Redis operations
   * with reactive support. It is used to interact with a Redis server for storing and retrieving data of type {@code Bot}
   * identified by a {@code String} key.
   */
  private final ReactiveRedisTemplate<String, Bot> redisTemplate;

  /**
   * Retrieves all the bots from the Redis server.
   *
   * @return a Flux that emits all the bots retrieved from Redis.
   */
  @GetMapping
  public Flux<Bot> getAllBots() {
    final int delayMillis = 100;
    return this.redisTemplate.scan()
        .flatMap(id -> this.redisTemplate.opsForValue().get(id))
        .delayElements(Duration.ofMillis(delayMillis))
        .doOnNext(bot -> log.info("[bot_id: {}] was retrieved from Redis", bot.botId()));
  }

}
