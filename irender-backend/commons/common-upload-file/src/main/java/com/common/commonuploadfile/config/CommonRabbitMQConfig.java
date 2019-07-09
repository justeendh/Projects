package com.common.commonuploadfile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class CommonRabbitMQConfig {

  @Value("${queue.upload-file.time-delay}")
  private Long timeDelay;

  @Value("${queue.upload-file.exchange}")
  private String exchange;

  @Value("${queue.upload-file.queue}")
  private String queueName;

  @Value("${queue.upload-file.routingKey}")
  private String routingKey;

  @Value("${queue.upload-file.retry.exchange}")
  private String retryExchange;

  @Value("${queue.upload-file.retry.queue}")
  private String retryQueueName;

  @Value("${queue.upload-file.retry.routingKey}")
  private String retryRoutingKey;

  @Bean
  public DirectExchange directExchange() {
    return new DirectExchange(exchange, true, false);
  }

  @Bean
  Queue queue() {
    return new Queue(queueName);
  }

  @Bean
  public Binding uploadBinding(DirectExchange directExchange, Queue queue) {
    return BindingBuilder.bind(queue).to(directExchange).with(routingKey);
  }

  @Bean
  public DirectExchange retryExchange() {
    return new DirectExchange(retryExchange, true, false);
  }

  @Bean
  Queue retryQueue() {
    Map queueArgs = new HashMap();
    queueArgs.put("x-dead-letter-exchange", exchange);
    queueArgs.put("x-message-ttl", timeDelay);
    queueArgs.put("x-dead-letter-routing-key", routingKey);
    return new Queue(retryQueueName, true, false, false, queueArgs);
  }

  @Bean
  public Binding retryBinding(DirectExchange retryExchange, Queue retryQueue) {
    return BindingBuilder.bind(retryQueue).to(retryExchange).with(retryRoutingKey);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

}
