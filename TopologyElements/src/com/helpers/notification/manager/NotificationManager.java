package com.helpers.notification.manager;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.filters.impl.TypeNotificationFilter;
import com.helpers.notification.handlers.NotificationHandler;
import com.helpers.notification.handlers.impl.LogNotificationHandler;
import com.helpers.notification.messages.NotificationMessage;
import com.helpers.notification.messages.NotificationType;
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

  static {
    //load notification handlers
    List<String> classNames = new ArrayList<>();
    classNames.add("com.helpers.notification.handlers.log.LogNotificationHandler");
    for (String in: classNames) {
      Class className = null;
      try {
        className = Class.forName(in);
      } catch (ClassNotFoundException e) {
        log.error("Could not find instance of Notification Handler class", e);
      }
      if (className!=null) {
        if (!(NotificationHandler.class.isAssignableFrom(className))) {
          log.error("Class " + className.getSimpleName() + " not an instance of a NotificationHandler");
        } else {
          try {
            NotificationHandler hdlr = (NotificationHandler)className.newInstance();
            List<NotificationFilter> filters = new ArrayList<>();
            filters.add(new TypeNotificationFilter(NotificationType.TENotification));
            NotificationManager.addNotificationProcessor(filters, hdlr);
          } catch (InstantiationException | IllegalAccessException e) {
            log.error("Could not instantiate class. Please check if default constructor is implemented", e);
          }
        }
      }
    }
  }

}
