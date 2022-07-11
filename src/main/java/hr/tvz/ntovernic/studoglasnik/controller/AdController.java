package hr.tvz.ntovernic.studoglasnik.controller;

import hr.tvz.ntovernic.studoglasnik.dto.AdCreateDto;
import hr.tvz.ntovernic.studoglasnik.dto.AdDto;
import hr.tvz.ntovernic.studoglasnik.dto.AdUpdateDto;
import hr.tvz.ntovernic.studoglasnik.dto.Filter;
import hr.tvz.ntovernic.studoglasnik.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/ad")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @GetMapping("/{adId}")
    public AdDto getAdById(@PathVariable final Long adId) {
        return adService.getAdById(adId);
    }

    @PostMapping("/all")
    public Page<AdDto> getAdsFiltered(final Pageable pageable, @RequestBody final Filter filter) {
        return adService.getAdsFiltered(pageable, filter);
    }

    @PostMapping("/all-count")
    public Integer getAdsFilteredCount(@RequestBody final Filter filter) {
        return adService.getAdsFilteredCount(filter);
    }

    @PostMapping("/all/user/{userId}")
    public Page<AdDto> getAdsFilteredByUser(@PathVariable final Long userId, @RequestBody final Filter filter,
                                            final Pageable pageable) {
        return adService.getAdsFilteredByUser(userId, pageable, filter);
    }

    @PostMapping("/all-count/user/{userId}")
    public Integer getAdsFilteredByUserCount(@PathVariable final Long userId, @RequestBody final Filter filter) {
        return adService.getAdsFilteredCountByUser(userId, filter);
    }

    @PostMapping
    public void createAd(@RequestPart("ad") final AdCreateDto adDto,
                         @RequestPart(value = "picture", required = false) final List<MultipartFile> pictureFiles) throws IOException {
        adService.createAd(adDto, pictureFiles);
    }

    @PutMapping("/{adId}")
    public void updateAd(@PathVariable final Long adId, @RequestPart("ad") final AdUpdateDto adDto,
                         @RequestPart(value = "picture", required = false) final List<MultipartFile> pictureFiles) throws IOException {
        adService.updateAd(adId, adDto, pictureFiles);
    }

    @DeleteMapping("/{adId}")
    public void deleteAd(@PathVariable final Long adId) throws IOException {
        adService.deleteAd(adId);
    }
}
