package hr.tvz.ntovernic.studoglasnik.dto;

import hr.tvz.ntovernic.studoglasnik.model.Category;
import hr.tvz.ntovernic.studoglasnik.model.Location;
import lombok.Data;

@Data
public class Filter {

    private String title;
    private Category category;
    private Location location;
    private Double priceFrom;
    private Double priceTo;
    private Boolean expired;
}
