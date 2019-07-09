package cf.thegc.bugatti.logging;

import cf.thegc.bugatti.BugattiApplication;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.LoggingEnhancer;

public class StackDriverLoggingEnhancer implements LoggingEnhancer {

    @Override
    public void enhanceLogEntry(LogEntry.Builder logEntry) {
        logEntry.addLabel("application_name", BugattiApplication.APPLICATION_NAME);
        logEntry.addLabel("application_version", BugattiApplication.APPLICATION_VERSION);
    }
}
