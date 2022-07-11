package hr.tvz.ntovernic.studoglasnik.service;

import hr.tvz.ntovernic.studoglasnik.dto.AdCreateDto;
import hr.tvz.ntovernic.studoglasnik.dto.AdDto;
import hr.tvz.ntovernic.studoglasnik.dto.AdUpdateDto;
import hr.tvz.ntovernic.studoglasnik.dto.Filter;
import hr.tvz.ntovernic.studoglasnik.model.Ad;
import hr.tvz.ntovernic.studoglasnik.properties.FolderProperties;
import hr.tvz.ntovernic.studoglasnik.repository.AdRepository;
import hr.tvz.ntovernic.studoglasnik.util.FilenameGenerator;
import hr.tvz.ntovernic.studoglasnik.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdService {

    private final FolderProperties folderProperties;
    private final AdRepository adRepository;

    public AdDto getAdById(final Long adId) {
        final Ad ad = getAd(adId);

        return MapperUtils.map(ad, AdDto.class);
    }

    public Page<AdDto> getAdsFiltered(final Pageable pageable, final Filter filter) {
        final Page<Ad> adPage = adRepository.findAllFiltered(filter, pageable);

        return MapperUtils.mapAll(adPage, AdDto.class);
    }

    public Integer getAdsFilteredCount(final Filter filter) {
        return adRepository.countAllFiltered(filter);
    }

    public Page<AdDto> getAdsFilteredByUser(final Long userId, final Pageable pageable, final Filter filter) {
        final Page<Ad> adPage = adRepository.findAllFilteredByUser(userId, filter, pageable);

        return MapperUtils.mapAll(adPage, AdDto.class);
    }

    public Integer getAdsFilteredCountByUser(final Long userId, final Filter filter) {
        return adRepository.countAllFilteredByUser(userId, filter);
    }

    public void createAd(final AdCreateDto adDto, final List<MultipartFile> pictureFiles) throws IOException {
        final Ad ad = MapperUtils.map(adDto, Ad.class);
        final LocalDateTime publishDate = LocalDateTime.now();
        final LocalDateTime expireDate = publishDate.plusDays(adDto.getDuration());
        final List<String> pictures = saveNewPictures(pictureFiles, publishDate);

        ad.setPublishDate(publishDate);
        ad.setExpireDate(expireDate);
        ad.setPictures(pictures);
        adRepository.save(ad);
    }

    public void updateAd(final Long adId, final AdUpdateDto adDto, final List<MultipartFile> pictureFiles) throws IOException {
        final Ad ad = getAd(adId);
        final List<String> updatedPictures = adDto.getPictures();
        final List<String> newPictures = saveNewPictures(pictureFiles, ad.getPublishDate());

        deleteRemovedPictures(ad.getPictures(), updatedPictures);
        updatedPictures.addAll(newPictures);

        BeanUtils.copyProperties(adDto, ad);
        ad.setPictures(updatedPictures);
        adRepository.save(ad);
    }

    public void deleteAd(final Long adId) throws IOException {
        final Ad ad = getAd(adId);

        deletePictures(ad.getPictures());
        adRepository.deleteById(adId);
    }

    private Ad getAd(final Long adId) {
        return adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Ad with id %d not found!", adId)));
    }

    private List<String> saveNewPictures(final List<MultipartFile> pictureFiles, final LocalDateTime publishDate) throws IOException {
        final List<String> newPictureList = new ArrayList<>();

        if(pictureFiles != null) {
            for(final MultipartFile pictureFile : pictureFiles) {
                final Path pictureFolder = Paths.get(folderProperties.getPictureFolder());
                final String pictureFileName = FilenameGenerator.generateFilename(publishDate, pictureFile.getOriginalFilename());
                Files.copy(pictureFile.getInputStream(), pictureFolder.resolve(pictureFileName));

                newPictureList.add(pictureFileName);
            }
        }

        return newPictureList;
    }

    private void deleteRemovedPictures(final List<String> allPictures, final List<String> updatedPictures) throws IOException {
        final List<String> picturesToDelete = allPictures.stream()
                .filter(picture -> !updatedPictures.contains(picture))
                .collect(Collectors.toList());

        deletePictures(picturesToDelete);
    }

    private void deletePictures(final List<String> pictureList) throws IOException {
        final Path pictureFolder = Paths.get(folderProperties.getPictureFolder());
        for(final String picture: pictureList) {
            Files.delete(pictureFolder.resolve(picture));
        }
    }
}
