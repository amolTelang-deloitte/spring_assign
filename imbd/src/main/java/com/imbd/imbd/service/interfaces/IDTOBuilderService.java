package com.imbd.imbd.service.interfaces;

import com.imbd.imbd.dto.PaginatedResponseDTO;
import com.imbd.imbd.dto.ResponseDTO;
import com.imbd.imbd.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public interface IDTOBuilderService {
    <T> ResponseDTO<T> createResponse(T data, HttpStatus status, String message);
    PaginatedResponseDTO<Movie> createPaginatedData(Page<Movie> data, HttpStatus status, String message);
}
