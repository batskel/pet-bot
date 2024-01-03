package pl.pet.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Provides configuration for Redis in a Spring application.
 */
@Configuration
public class RedisConfiguration {

  /**
   * Creates a ReactiveRedisTemplate object that provides operations for interacting with Redis in a reactive way.
   *
   * @param factory the ReactiveRedisConnectionFactory object representing the connection to Redis.
   * @param <T> the type of objects stored in Redis.
   * @return a ReactiveRedisTemplate<String, T> object.
   */
  @Bean
  public <T> ReactiveRedisTemplate<String, T> reactiveRedisTemplate(final ReactiveRedisConnectionFactory factory) {
    final RedisSerializer<Object> redisSerializer = new GenericJackson2JsonRedisSerializer();

    final RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder =
        RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

    final RedisSerializationContext<String, T> context = builder.value((RedisSerializer<T>) redisSerializer).build();
    return new ReactiveRedisTemplate<>(factory, context);
  }

}
