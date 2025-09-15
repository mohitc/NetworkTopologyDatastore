package io.github.mohitc.helpers.notification.manager;

import io.github.mohitc.helpers.notification.filters.NotificationFilter;
import io.github.mohitc.helpers.notification.handlers.NotificationHandler;
import io.github.mohitc.helpers.notification.manager.parser.NotificationHandlerParser;
import io.github.mohitc.helpers.notification.manager.parser.NotificationProcessorConf;
import io.github.mohitc.helpers.notification.messages.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//General class to manage how notifications are forwarded
public final class NotificationManager {

  private static final Logger log = LoggerFactory.getLogger(NotificationManager.class);

  private static final Set<NotificationProcessor> notificationProcessors = new HashSet<>();

  private NotificationManager() {
  }

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
    try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
      executor.submit(processor);
    }
  }

  private static void initNotificationHandlerConf() {
    try {
      URL filePath = NotificationManager.class.getClassLoader().getResource("META-INF/notfhandlers.xml");
      if (filePath==null) {
        log.debug("Could not find or load notfhandlers.xml");
        return;
      }
      NotificationHandlerParser parser = new NotificationHandlerParser();
      List<NotificationProcessorConf> confList = parser.parse(filePath.toURI());

      if (confList!=null) {
        for (NotificationProcessorConf conf: confList) {
          addNotificationProcessor(conf.getFilters(), conf.getHandler());
        }
      }
    } catch (Exception e) {
      log.error("Could not find or load notfhandlers.xml.sample");
    }
  }

  static {
    initNotificationHandlerConf();
  }

  public static NotificationManager createNotificationManager() {
    return new NotificationManager();
  }
}
