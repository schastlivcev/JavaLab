package ru.kpfu.itis.rodsher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.Article;
import ru.kpfu.itis.rodsher.models.User;
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
                    article, null, false, false, null)) != null) {
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
    public Dto loadArticlesByUserIdWithReplies(Long userId) {
        return new WebDto(Status.ARTICLE_LOAD_BY_USER_ID_SUCCESS, "walls", wallsRepository.findByUserIdWithReplies(userId));
    }

    @Override
    public Dto loadArticlesByUserIdReplies(Long userId) {
        return new WebDto(Status.ARTICLE_LOAD_BY_USER_ID_REPLIES_SUCCESS, "walls", wallsRepository.findByUserIdReplies(userId));
    }

    @Override
    public Dto loadArticlesByUserIdBookmarks(Long userId) {
        return new WebDto(Status.ARTICLE_LOAD_BY_USER_ID_BOOKMARKS_SUCCESS, "walls", wallsRepository.findByUserIdBookmarks(userId));
    }

    @Override
    public Dto loadArticlesByUserIdAndArticleId(Long userId, Long articleId) {
        return new WebDto(Status.ARTICLE_LOAD_BY_USER_ID_AND_ARTICLE_ID_SUCCESS, "walls", wallsRepository.findByUserIdAndArticleId(userId, articleId));
    }

    @Override
    public Dto loadArticlesByUserFriends(Long userId) {
        return new WebDto(Status.ARTICLE_LOAD_BY_USER_FRIENDS_SUCCESS, "walls", wallsRepository.findByUserFriends(userId));
    }

    @Override
    public Dto deleteArticle(Long id) {
        if(wallsRepository.remove(id)) {
            return new WebDto(Status.ARTICLE_DELETE_SUCCESS);
        }
        return new WebDto(Status.ARTICLE_DELETE_ERROR);
    }

    @Override
    public Dto updateWall(Wall wall, String type, User user) {
        switch(type){
            case "DELETE":
                if(wall.getUser().getId().equals(user.getId()) && wallsRepository.remove(wall.getId())) {
                    return new WebDto(Status.ARTICLE_UPDATE_DELETE_SUCCESS);
                }
                return new WebDto(Status.ARTICLE_UPDATE_DELETE_ERROR);
            case "DELETE_REPLY":
                List<Wall> replies = wallsRepository.findByUserIdAndArticleId(user.getId(), wall.getArticle().getId());
                boolean replyRemoved = false;
                for(Wall found : replies) {
                    if(found.isReply()) {
                        if(wallsRepository.remove(found.getId())) {
                            replyRemoved = true;
                        }
                        break;
                    }
                }
                if(replyRemoved) {
                    return new WebDto(Status.ARTICLE_UPDATE_DELETE_REPLY_SUCCESS);
                }
                return new WebDto(Status.ARTICLE_UPDATE_DELETE_REPLY_ERROR);
            case "DELETE_BOOKMARK":
                List<Wall> bookmarks = wallsRepository.findByUserIdAndArticleId(user.getId(), wall.getArticle().getId());
                boolean bookmarkRemoved = false;
                for(Wall found : bookmarks) {
                    if(found.isBookmark()) {
                        if(wallsRepository.remove(found.getId())) {
                            bookmarkRemoved = true;
                        }
                        break;
                    }
                }
                if(bookmarkRemoved) {
                    return new WebDto(Status.ARTICLE_UPDATE_DELETE_BOOKMARK_SUCCESS);
                }
                return new WebDto(Status.ARTICLE_UPDATE_DELETE_BOOKMARK_ERROR);
            case "REPLY":
                if(!wall.getArticle().getUser().getId().equals(user.getId()) && wallsRepository.save(new Wall(null, user, wall.getArticle(), wall, true, false, null)) != null) {
                    return new WebDto(Status.ARTICLE_UPDATE_REPLY_SUCCESS);
                }
                return new WebDto(Status.ARTICLE_UPDATE_REPLY_ERROR);
            case "BOOKMARK":
                if(!wall.getArticle().getUser().getId().equals(user.getId()) && wallsRepository.save(new Wall(null, user, wall.getArticle(), wall, false, true, null)) != null) {
                    return new WebDto(Status.ARTICLE_UPDATE_BOOKMARK_SUCCESS);
                }
                return new WebDto(Status.ARTICLE_UPDATE_BOOKMARK_ERROR);
            default:
                return new WebDto(Status.ARTICLE_UPDATE_ERROR);
        }
    }
}
