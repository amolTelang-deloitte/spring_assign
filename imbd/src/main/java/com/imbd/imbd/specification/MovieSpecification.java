package com.imbd.imbd.specification;

import com.imbd.imbd.entity.Movie;
import com.imbd.imbd.enums.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

public class MovieSpecification {
    private final static Logger logger = LoggerFactory.getLogger(MovieSpecification.class);

    public static Specification<Movie> withCriteria(String studio, String language, String director,String releaseYear,String genre) {
        return(root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(genre != null && !genre.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("genre"), Genre.valueOf(genre)));
            }
            if (studio != null && !studio.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("studio"), studio));
            }
            if (language != null && !language.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("language"), language));
            }
            if (director != null && !director.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("director"), director));
            }
            if (releaseYear != null && !releaseYear.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("releaseYear"), releaseYear));
            }

            logger.info("Building Specification with predicates"+" "+predicates+" "+ LocalDateTime.now());
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}