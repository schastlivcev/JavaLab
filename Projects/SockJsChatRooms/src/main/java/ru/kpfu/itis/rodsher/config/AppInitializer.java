package ru.kpfu.itis.rodsher.config;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.IOException;

public class AppInitializer { //implements WebApplicationInitializer {

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
//        PropertySource propertySource;
//        try {
//            propertySource = new ResourcePropertySource("classpath:app.properties");
//        } catch (IOException e) {
//            throw new IllegalStateException("File 'app.properties' not found.");
//        }
//        appContext.getEnvironment().setActiveProfiles((String) propertySource.getProperty("spring.profile"));
//        appContext.register(AppConfig.class);
//        appContext.register(WebConfig.class);
//        servletContext.addListener(new ContextLoaderListener(appContext));
//
//        ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("dispatcher", new DispatcherServlet(appContext));
//        dispatcherServlet.setLoadOnStartup(1);
//        dispatcherServlet.addMapping("/");
//    }
}