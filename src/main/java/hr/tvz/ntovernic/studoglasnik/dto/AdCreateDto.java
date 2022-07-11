package hr.tvz.ntovernic.studoglasnik.dto;

import lombok.Data;

@Data
public class AdCreateDto {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private Integer duration;
    private UserDto contactUser;
    private CategoryDto category;
    private LocationDto location;
}
