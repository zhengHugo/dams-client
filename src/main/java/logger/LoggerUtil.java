package logger;

import model.common.ClientId;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class LoggerUtil {
  // create a logger for a client id
  public static void createLoggerByClientId(ClientId clientId) {
    final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    final Configuration config = ctx.getConfiguration();
    //    final Layout<String> layout = PatternLayout.createDefaultLayout(config);
    Layout<String> layout =
        PatternLayout.newBuilder()
            .withConfiguration(config)
            .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
            .build();

    Appender fileAppender =
        FileAppender.newBuilder()
            .withFileName("logs/" + clientId.getId() + ".log")
            .setName("FileAppender")
            .setLayout(layout)
            .withAppend(false)
            .setConfiguration(config)
            .build();
    fileAppender.start();

    Appender consoleAppender =
        ConsoleAppender.newBuilder()
            .setName("ConsoleAppender")
            .setLayout(layout)
            .setConfiguration(config)
            .build();
    consoleAppender.start();

    config.addAppender(fileAppender);
    AppenderRef fileAppenderRef = AppenderRef.createAppenderRef("FileAppender", null, null);
    config.addAppender(consoleAppender);
    AppenderRef consoleAppenderRef = AppenderRef.createAppenderRef("ConsoleAppender", null, null);
    AppenderRef[] refs = new AppenderRef[] {fileAppenderRef, consoleAppenderRef};
    LoggerConfig loggerConfig =
        LoggerConfig.createLogger(
            false, Level.TRACE, "logger." + clientId.getId(), "true", refs, null, config, null);
    loggerConfig.addAppender(fileAppender, null, null);
    loggerConfig.addAppender(consoleAppender, null, null);
    config.addLogger("logger." + clientId.getId(), loggerConfig);
    ctx.updateLoggers();
  }
}
