package org.sopt.starbucks.domain.image.domain;

import java.util.List;
import java.util.Optional;

public interface ImageRepository {

    Optional<Image> findByImagePurposeAndMenuId(ImagePurpose imagePurpose, Long menuId);

    // 홈 화면만 타겟팅
    List<Image> findAllByMenuIdInAndImagePurposeIn(List<Long> menuIds, List<ImagePurpose> imagePurposes);
}
