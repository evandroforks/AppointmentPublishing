package com.appointment.publishing.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * Show an body error message when an unknown JSON parameter is used.
 *
 * <p>Forces spring to show an error message when FAIL_ON_UNKNOWN_PROPERTIES is set, instead of just
 * rejecting the request with status 400 and no body message informing what went wrong.
 *
 * @see <a href="https://stackoverflow.com/questions/5455014">Ignoring new fields on JSON objects
 *     using Jackson</a>
 * @see <a href="https://stackoverflow.com/questions/46644099">Can't set ProblemHandler to
 *     ObjectMapper in Spring Boot</a>
 */
@SpringBootConfiguration
public class JacksonUnknownPropertyConfig {
  private static final Logger logger = LoggerFactory.getLogger(JacksonUnknownPropertyConfig.class);

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer customizer() {
    return new Jackson2ObjectMapperBuilderCustomizer() {
      @Override
      public void customize(Jackson2ObjectMapperBuilder builder) {
        builder.modules(new MyModule());
      }
    };
  }

  private static class MyModule extends SimpleModule {
    @Override
    public void setupModule(SetupContext context) {
      // Required, as documented in the Javadoc of SimpleModule
      super.setupModule(context);
      context.addDeserializationProblemHandler(new MyDeserializationProblemHandler());
    }
  }

  private static class MyDeserializationProblemHandler extends DeserializationProblemHandler {
    @Override
    public boolean handleUnknownProperty(
        DeserializationContext ctxt,
        JsonParser p,
        JsonDeserializer<?> deserializer,
        Object beanOrClass,
        String propertyName)
        throws IOException {

      final String missing = String.format("Unknown request property '%s'", propertyName);
      logger.warn(missing);

      if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missing);
      }
      return super.handleUnknownProperty(ctxt, p, deserializer, beanOrClass, propertyName);
    }
  }
}
