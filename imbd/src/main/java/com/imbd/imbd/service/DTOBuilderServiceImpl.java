package com.imbd.imbd.service;

import com.imbd.imbd.dto.PaginatedResponseDTO;
import com.imbd.imbd.dto.ResponseDTO;
import com.imbd.imbd.entity.Movie;
import com.imbd.imbd.service.interfaces.IDTOBuilderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DTOBuilderServiceImpl implements IDTOBuilderService {
    @Override
    public <T> ResponseDTO<T> createResponse(T data, HttpStatus status, String message) {
        return ResponseDTO.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    @Override
    public PaginatedResponseDTO<Movie> createPaginatedData(Page<Movie> data, HttpStatus status, String message) {
        return PaginatedResponseDTO.<Movie>builder()
                .status(status)
                .message(message)
                .data(data.getContent())
                .pageNumber(data.getNumber())
                .pageSize(data.getSize())
                .totalElements(data.getTotalElements())
                .totalPages(data.getTotalPages())
                .build();
    }
}
