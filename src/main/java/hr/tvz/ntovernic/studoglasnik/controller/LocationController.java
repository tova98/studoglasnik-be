package hr.tvz.ntovernic.studoglasnik.controller;

import hr.tvz.ntovernic.studoglasnik.dto.LocationDto;
import hr.tvz.ntovernic.studoglasnik.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/all")
    public List<LocationDto> getLocations() {
        return locationService.getLocations();
    }

    @GetMapping("/{locationId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public LocationDto getLocationById(@PathVariable final Long locationId) {
        return locationService.getLocationById(locationId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createLocation(@RequestBody final LocationDto locationDto) {
        locationService.createLocation(locationDto);
    }

    @PutMapping("/{locationId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateLocation(@PathVariable final Long locationId, @RequestBody final LocationDto locationDto) {
        locationService.updateLocation(locationId, locationDto);
    }

    @DeleteMapping("/{locationId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteLocation(@PathVariable final Long locationId) {
        locationService.deleteLocation(locationId);
    }
}
