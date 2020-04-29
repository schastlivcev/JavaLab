package ru.kpfu.itis.rodsher.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.Article;
import ru.kpfu.itis.rodsher.models.Wall;
import ru.kpfu.itis.rodsher.services.WallArticleService;

import java.util.Collections;
import java.util.Set;

@Component
public class StringIdToWallConverter implements ConditionalGenericConverter {
    @Autowired
    private WallArticleService wallArticleService;

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getAnnotation(ConvertToArticle.class) != null;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Wall.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            Long id = Long.parseLong((String) source);
            Dto dto = wallArticleService.loadArticle(id);
            if(dto.getStatus().equals(Status.ARTICLE_LOAD_SUCCESS)) {
                return dto.get("wall");
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
