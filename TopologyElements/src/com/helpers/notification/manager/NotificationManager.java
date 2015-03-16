package com.helpers.notification.manager;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.messages.NotificationMessage;

import java.util.LinkedList;
import java.util.List;

//General class to manage how notifications are forwarded
public class NotificationManager {

  private static List<NotificationMessage> incomingMessages = new LinkedList<>();

  private static List<NotificationFilter> incomingFilters = new LinkedList<>();

}
