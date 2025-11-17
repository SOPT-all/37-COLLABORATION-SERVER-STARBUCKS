package org.sopt.starbucks.domain.image.infrastructure;

import org.sopt.starbucks.domain.image.domain.Image;
import org.sopt.starbucks.domain.image.domain.ImagePurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByImagePurposeAndMenuId(ImagePurpose imagePurpose, Long menuId);

    // 홈 화면만 타겟팅
    List<Image> findAllByMenuIdInAndImagePurposeIn(List<Long> menuIds, List<ImagePurpose> imagePurposes);
}
