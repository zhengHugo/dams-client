package logger;

import model.common.ClientId;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class CustomConfigFactory extends ConfigurationFactory {
  private final ClientId clientId;

  public CustomConfigFactory(ClientId clientId) {
    super();
    this.clientId = clientId;
  }

  @Override
  protected String[] getSupportedTypes() {
    return new String[] {"*"};
  }

  @Override
  public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
    ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();

    // layout
    LayoutComponentBuilder layout = builder.newLayout("PatternLayout");
    layout.addAttribute("pattern", "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n");

    // file appender
    AppenderComponentBuilder file = builder.newAppender("file", "File");
    file.addAttribute("fileName", "logs/" + clientId.getId() + ".log");
    file.addAttribute("append", "false");
    file.add(layout);
    builder.add(file);

    // console appender
    AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
    console.addAttribute("target", "SYSTEM_OUT");
    console.add(layout);
    builder.add(console);

    // create log
    LoggerComponentBuilder logger = builder.newLogger(clientId.getId(), Level.TRACE);
    logger.add(builder.newAppenderRef("file"));
    builder.add(logger);
    return builder.build();
  }
}
