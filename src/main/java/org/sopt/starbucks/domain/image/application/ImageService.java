package org.sopt.starbucks.domain.image.application;

import org.sopt.starbucks.domain.image.domain.Image;
import org.sopt.starbucks.domain.image.domain.ImagePurpose;

import java.util.List;

public interface ImageService {

    Image getImage(ImagePurpose imagePurpose, Long menuId);

    // 홈 화면만 타겟팅
    List<Image> findAllByMenuIdInAndImagePurposeIn(List<Long> menuIds, List<ImagePurpose> imagePurposes);
}
