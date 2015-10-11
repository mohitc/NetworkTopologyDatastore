package test.topology.helpers;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.filters.impl.TypeNotificationFilter;
import com.helpers.notification.handlers.impl.LogNotificationHandler;
import com.helpers.notification.manager.NotificationManager;
import com.helpers.notification.messages.NotificationType;

import java.util.ArrayList;
import java.util.List;

public class InitNotificationManager {

  public static void init(){
    List<NotificationFilter> filters = new ArrayList<>();
    filters.add(new TypeNotificationFilter(NotificationType.TENotification));
//    NotificationManager.addNotificationProcessor(filters, new LogNotificationHandler());
  }
}
