package com.imbd.imbd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailsDTO {
    private String movieName;
    private String description;
    private String language;
    private String studio;
    private Integer duration;
    private String producer;
    private String director;
    private String releaseYear;
    private String genre;
}
