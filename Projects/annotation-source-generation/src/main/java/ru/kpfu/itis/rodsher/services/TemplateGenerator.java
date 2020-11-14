package ru.kpfu.itis.rodsher.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class TemplateGenerator {
    public static final String TEMPLATES_DIRECTORY;
    private static final String DEFAULT_ENCODING = "UTF-8";

    private Configuration configuration;

    static {
        TEMPLATES_DIRECTORY = TemplateGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public TemplateGenerator(String templatesDirectory) {
        configuration = new Configuration(Configuration.VERSION_2_3_30);
        try {
            configuration.setDirectoryForTemplateLoading(new File(templatesDirectory));
            configuration.setDefaultEncoding(DEFAULT_ENCODING);
        } catch (IOException e) {
            throw new IllegalStateException("Can not find template loading directory: " + TEMPLATES_DIRECTORY, e);
        }
    }

    public TemplateGenerator() {
        this(TEMPLATES_DIRECTORY);
    }

    public void generateTemplate(String templateName, Map<String, Object> attributes, String filePath) {
        try(FileWriter fw = new FileWriter(filePath)) {
            Template template = configuration.getTemplate(templateName);
            template.process(attributes, fw);
        } catch (IOException | TemplateException e) {
            throw new IllegalArgumentException("Failed to generate Template: " + TEMPLATES_DIRECTORY, e);
        }
    }
}