package org.sopt.starbucks.domain.image.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.starbucks.domain.menu.domain.Menu;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "image")
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_purpose", nullable = false)
    private ImagePurpose imagePurpose;

    @Column(name = "image_url", length = 1000, nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Builder
    public Image(ImagePurpose imagePurpose, String imageUrl, Menu menu) {
        this.imagePurpose = imagePurpose;
        this.imageUrl = imageUrl;
        this.menu = menu;
    }
}
