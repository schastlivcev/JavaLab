package ru.kpfu.itis.rodsher.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.models.Article;
import ru.kpfu.itis.rodsher.models.Country;
import ru.kpfu.itis.rodsher.models.User;

import java.util.Optional;

@Component
@Profile("template")
final class EntityLoader {
    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ArticlesRepository articlesRepository;

    Country getCountryById(int countryId) {
        Optional<Country> countryOptional = countriesRepository.findById(countryId);
        if(countryOptional.isPresent()) {
            return countryOptional.get();
        }
        throw new IllegalArgumentException("Invalid country id");
    }

    User getUserById(long userId) {
        Optional<User> userOptional = usersRepository.findById(userId);
        if(userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new IllegalArgumentException("Invalid country id");
    }

    Article getArticleById(long articleId) {
        Optional<Article> articleOptional = articlesRepository.findById(articleId);
        if(articleOptional.isPresent()) {
            return articleOptional.get();
        }
        throw new IllegalArgumentException("Invalid country id");
    }
}
