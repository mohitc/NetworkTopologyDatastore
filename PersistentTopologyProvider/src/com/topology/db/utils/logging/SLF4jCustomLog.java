package com.topology.db.utils.logging;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4jCustomLog extends AbstractSessionLog {

  private static Logger log = LoggerFactory.getLogger("Persistence");

  @Override
  public void log(SessionLogEntry sessionLogEntry) {
    if (sessionLogEntry.getException()!=null) {
      switch (sessionLogEntry.getLevel()) {
        default: log.info(sessionLogEntry.getMessage(), sessionLogEntry.getException());
      }
    } else {
      switch (sessionLogEntry.getLevel()) {
        default: log.info(sessionLogEntry.getMessage());
      }
    }
  }
}
