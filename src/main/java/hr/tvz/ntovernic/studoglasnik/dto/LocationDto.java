package hr.tvz.ntovernic.studoglasnik.dto;

import lombok.Data;

@Data
public class LocationDto {

    private Long id;
    private String name;
    private String address;
    private String googleMapsLink;
}
