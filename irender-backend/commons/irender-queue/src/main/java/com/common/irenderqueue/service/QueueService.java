package com.common.irenderqueue.service;

public interface QueueService {

  void send(String exchange, String routingKey, Object data);

  void send(String exchange, String routingKey, Object data, long timeDelay);
}
