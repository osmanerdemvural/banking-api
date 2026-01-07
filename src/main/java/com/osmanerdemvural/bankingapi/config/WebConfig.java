package com.osmanerdemvural.bankingapi.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("dev")
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:5173")
        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(false);
  }
}
