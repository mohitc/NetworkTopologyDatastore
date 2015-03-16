package com.helpers.notification.manager;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.handlers.NotificationHandler;
import com.helpers.notification.messages.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

//General class to manage how notifications are forwarded
public class NotificationManager {

  private static final Logger log = LoggerFactory.getLogger(NotificationManager.class);

  private static Set<NotificationProcessor> notificationProcessors = new HashSet<>();

  public synchronized static void receiveNotification(NotificationMessage message){
    if (message==null) {
      log.error("Null notification message received");
      return;
    }
    for (NotificationProcessor processor: notificationProcessors) {
      processor.addMessage(message);
    }
  }

  public static void addNotificationProcessor(List<NotificationFilter> filters, NotificationHandler handler) {
    NotificationProcessor processor = new NotificationProcessor(handler);
    processor.addFilters(filters);
    notificationProcessors.add(processor);
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(processor);
  }

}
