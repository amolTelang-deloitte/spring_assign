package com.imbd.imbd.repository;

import com.imbd.imbd.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MovieRepository extends JpaRepository<Movie,Long>, JpaSpecificationExecutor<Movie>, PagingAndSortingRepository<Movie,Long> {
    Page<Movie> findAll(Pageable pageable);
    Movie findFirstBymovieName(String movieName);


}
