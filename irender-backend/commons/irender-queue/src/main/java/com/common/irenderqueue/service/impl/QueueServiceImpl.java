package com.common.irenderqueue.service.impl;

import com.common.irenderqueue.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueServiceImpl implements QueueService {

  private static final Logger LOGGER = LoggerFactory.getLogger(QueueServiceImpl.class);

  private static final String AMQP_EXCEPTION = "AmqpException ";

  private static final String SEND_DATA = "Send data to: ";

  @Autowired
  private AmqpTemplate amqpTemplate;


  public void send(String exchange, String routingKey, Object data) {
    try {
      amqpTemplate.convertAndSend(exchange, routingKey, data);
      LOGGER.info(SEND_DATA, exchange);
    } catch (AmqpException ex) {
      LOGGER.error(AMQP_EXCEPTION, ex.getMessage());
    }

  }


  public void send(String exchange, String routingKey, Object data, long timeDelay) {
    try {
      amqpTemplate.convertAndSend(exchange, routingKey, data, m -> {
        m.getMessageProperties().getHeaders().put("x-delay", timeDelay);
        return m;
      });
      LOGGER.info(SEND_DATA, exchange);
    } catch (AmqpException ex) {
      LOGGER.error(AMQP_EXCEPTION, ex.getMessage());
    }
  }


}
