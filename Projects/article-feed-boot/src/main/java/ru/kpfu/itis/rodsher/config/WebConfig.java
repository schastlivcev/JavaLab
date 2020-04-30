package ru.kpfu.itis.rodsher.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.kpfu.itis.rodsher.converters.StringIdToWallConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private StringIdToWallConverter converter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(converter);
    }
}
