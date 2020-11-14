package ru.kpfu.itis.rodsher.annotations;

import com.google.auto.service.AutoService;
import ru.kpfu.itis.rodsher.html.Form;
import ru.kpfu.itis.rodsher.html.Input;
import ru.kpfu.itis.rodsher.services.TemplateGenerator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes(value = {"ru.kpfu.itis.rodsher.annotations.HtmlForm"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class HtmlProcessor extends AbstractProcessor {
    private static TemplateGenerator templateGenerator;

    static {
        templateGenerator = new TemplateGenerator();
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(HtmlForm.class);
        Set<? extends Element> annotatedFields = roundEnv.getElementsAnnotatedWith(HtmlInput.class);
        for (Element element : annotatedElements) {
            Path path = Paths.get(HtmlProcessor.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)
                    + element.getSimpleName().toString() + ".html");
            processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                    "Creating HTML form for " + element.getSimpleName() + ".class along the path: " + path);

            Map<String, Object> model = new HashMap<>();

            HtmlForm htmlForm = element.getAnnotation(HtmlForm.class);
            model.put("form", Form.builder()
                    .method(htmlForm.method())
                    .action(htmlForm.action())
                    .name(htmlForm.name()).build());

            List<Input> inputs = new ArrayList<>();
            for(Element annotatedField : annotatedFields) {
                HtmlInput htmlInput = annotatedField.getAnnotation(HtmlInput.class);
                inputs.add(Input.builder()
                        .type(htmlInput.type())
                        .name(htmlInput.name())
                        .placeholder(htmlInput.placeholder()).build());
            }
            model.put("inputs", inputs);
            templateGenerator.generateTemplate("form.ftlh", model, path.toAbsolutePath().toString());
        }
        return true;
    }
}