package hr.tvz.ntovernic.studoglasnik.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdDto {

    private Long id;
    private String title;
    private String description;
    private Double price;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime publishDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime expireDate;

    private UserDto contactUser;
    private CategoryDto category;
    private LocationDto location;
    private List<String> pictures;
}
