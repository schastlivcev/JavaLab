package ru.kpfu.itis.rodsher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.Article;
import ru.kpfu.itis.rodsher.models.Wall;
import ru.kpfu.itis.rodsher.repositories.ArticlesRepository;
import ru.kpfu.itis.rodsher.repositories.WallsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WallArticleServiceImpl implements WallArticleService {
    @Autowired
    private ArticlesRepository articlesRepository;

    @Autowired
    private WallsRepository wallsRepository;

    @Override
    public Dto addArticle(Article article) {
        Long id = articlesRepository.save(article);
        if(id != null) {
            article.setId(id);
            if(wallsRepository.save(new Wall(null, article.getUser(),
                    article, false, false, null)) != null) {
                return new WebDto(Status.ARTICLE_ADD_SUCCESS);
            }
        }
        return new WebDto(Status.ARTICLE_ADD_ERROR);
    }

    @Override
    public Dto loadArticle(Long id) {
        Optional<Wall> wallOptional = wallsRepository.findById(id);
        if(wallOptional.isPresent()) {
            return new WebDto(Status.ARTICLE_LOAD_SUCCESS, "wall", wallOptional.get());
        }
        return new WebDto(Status.ARTICLE_LOAD_ERROR);
    }

    @Override
    public Dto loadArticlesByUserId(Long userId) {
        return new WebDto(Status.ARTICLE_LOAD_BY_USER_ID_SUCCESS, "walls", (List<Wall>) wallsRepository.findByUserId(userId));
    }

    @Override
    public Dto deleteArticle(Long id) {
        if(wallsRepository.remove(id)) {
            return new WebDto(Status.ARTICLE_DELETE_SUCCESS);
        }
        return new WebDto(Status.ARTICLE_DELETE_ERROR);
    }


}
