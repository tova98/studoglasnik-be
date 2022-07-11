package hr.tvz.ntovernic.studoglasnik.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Double price;
    private LocalDateTime publishDate;
    private LocalDateTime expireDate;

    @ManyToOne
    private User contactUser;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Location location;

    @Column(name = "PICTURE")
    @CollectionTable(name = "AD_PICTURE")
    @ElementCollection
    private List<String> pictures;
}
