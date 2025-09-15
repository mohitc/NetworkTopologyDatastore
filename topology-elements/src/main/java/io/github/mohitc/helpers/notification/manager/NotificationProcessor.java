package io.github.mohitc.helpers.notification.manager;

import io.github.mohitc.helpers.notification.filters.NotificationFilter;
import io.github.mohitc.helpers.notification.handlers.NotificationHandler;
import io.github.mohitc.helpers.notification.messages.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NotificationProcessor implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(NotificationProcessor.class);

  private final BlockingQueue<NotificationMessage> incoming;

  private final List<NotificationFilter> filters;

  private final NotificationHandler handler;

  public void addMessage(NotificationMessage message) {
    incoming.add(message);
  }

  public void addFilter(NotificationFilter filter){
    if (filter!=null) {
      filters.add(filter);
    }
  }

  public void addFilters(List<NotificationFilter> filters){
    if (filters!=null) {
      filters.forEach(this::addFilter);
    }
  }


  public List<NotificationFilter> getFilters() {
    return Collections.unmodifiableList(filters);
  }

  public NotificationProcessor(NotificationHandler handler) {
    incoming = new LinkedBlockingQueue<>();
    filters = new LinkedList<>();
    this.handler = handler;
  }

  public NotificationProcessor(List<NotificationFilter> filters, NotificationHandler handler) {
    incoming = new LinkedBlockingQueue<>();
    this.filters = new LinkedList<>();
    this.addFilters(filters);
    this.handler = handler;
  }

  private boolean doFilter(NotificationMessage message) {
    boolean filterAction = false;
    for (NotificationFilter filter: filters) {
      if (filter.doFilter(message)) {
        filterAction=true;
        break;
      }
    }
    return filterAction;
  }


  @Override
  public void run() {
    while(true) {
      try {
        NotificationMessage message = incoming.take();
        if(!doFilter(message)) {
          handler.handle(message);
        }
      } catch (InterruptedException e) {
        //TODO Interrupted exception can indicate a shutdown call, include proper handling
        break;
      } catch (Exception e) {
        log.error("Unexpected Exception: ", e);
      }
    }
  }
}
