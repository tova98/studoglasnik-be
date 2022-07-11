package hr.tvz.ntovernic.studoglasnik.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdUpdateDto {

    private String title;
    private String description;
    private Double price;
    private CategoryDto category;
    private LocationDto location;
    private List<String> pictures;
}
