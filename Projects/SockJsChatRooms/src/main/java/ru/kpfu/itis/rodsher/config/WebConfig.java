package ru.kpfu.itis.rodsher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

//@Configuration
//@EnableWebMvc
//@ComponentScan("ru.kpfu.itis.rodsher.controllers")
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setPrefix("");
        freeMarkerViewResolver.setSuffix(".ftl");
        freeMarkerViewResolver.setContentType("text/html; charset=utf-8");
        freeMarkerViewResolver.setRequestContextAttribute("rc");
        freeMarkerViewResolver.setCache(true);
        freeMarkerViewResolver.setRedirectContextRelative(false);
        return freeMarkerViewResolver;
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setDefaultEncoding("UTF-8");
        freeMarkerConfigurer.setTemplateLoaderPath("WEB-INF/views/ftl/");
        return freeMarkerConfigurer;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }
}
