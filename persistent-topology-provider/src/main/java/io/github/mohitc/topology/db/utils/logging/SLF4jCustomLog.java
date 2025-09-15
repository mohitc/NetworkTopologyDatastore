package io.github.mohitc.topology.db.utils.logging;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4jCustomLog extends AbstractSessionLog {

  private static final Logger log = LoggerFactory.getLogger("Persistence");

  @Override
  public void log(SessionLogEntry sessionLogEntry) {
    if (sessionLogEntry.getException()!=null) {
      if (log.isInfoEnabled()) {
        log.info(sessionLogEntry.getMessage(), sessionLogEntry.getException());
      }
    } else {
      if (log.isInfoEnabled()) {
        log.info(sessionLogEntry.getMessage());
      }
    }
  }
}
