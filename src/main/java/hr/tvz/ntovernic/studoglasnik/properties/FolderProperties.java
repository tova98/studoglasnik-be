package hr.tvz.ntovernic.studoglasnik.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Component
@Validated
@ConfigurationProperties("hr.tvz.ntovernic.studoglasnik")
public class FolderProperties {

    @NotBlank
    private String pictureFolder;

    @NotBlank
    private String userPictureFolder;
}
