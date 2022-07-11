package hr.tvz.ntovernic.studoglasnik.service;

import hr.tvz.ntovernic.studoglasnik.dto.LocationDto;
import hr.tvz.ntovernic.studoglasnik.model.Location;
import hr.tvz.ntovernic.studoglasnik.repository.LocationRepository;
import hr.tvz.ntovernic.studoglasnik.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationDto> getLocations() {
        final List<Location> locationList = locationRepository.findAll();

        return MapperUtils.mapAll(locationList, LocationDto.class);
    }

    public LocationDto getLocationById(final Long locationId) {
        final Location location = getLocation(locationId);

        return MapperUtils.map(location, LocationDto.class);
    }

    public void createLocation(final LocationDto locationDto) {
        final Location location = MapperUtils.map(locationDto, Location.class);
        locationRepository.save(location);
    }

    public void updateLocation(final Long locationId, final LocationDto locationDto) {
        final Location location = getLocation(locationId);

        BeanUtils.copyProperties(locationDto, location, "id");
        locationRepository.save(location);
    }

    public void deleteLocation(final Long locationId) {
        final Location location = getLocation(locationId);
        locationRepository.delete(location);
    }

    private Location getLocation(final Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Location with id %d not found!", locationId)));
    }
}
