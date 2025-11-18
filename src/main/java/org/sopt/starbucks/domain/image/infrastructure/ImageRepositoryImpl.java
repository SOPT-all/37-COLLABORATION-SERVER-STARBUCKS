package org.sopt.starbucks.domain.image.infrastructure;

import lombok.RequiredArgsConstructor;
import org.sopt.starbucks.domain.image.domain.Image;
import org.sopt.starbucks.domain.image.domain.ImagePurpose;
import org.sopt.starbucks.domain.image.domain.ImageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;

    @Override
    public Optional<Image> findByImagePurposeAndMenuId(ImagePurpose imagePurpose, Long menuId) {
        return imageJpaRepository.findByImagePurposeAndMenuId(imagePurpose, menuId);
    }

    @Override
    public List<Image> findAllByMenuIdInAndImagePurposeIn(List<Long> menuIds, List<ImagePurpose> imagePurposes) {
        return imageJpaRepository.findAllByMenuIdInAndImagePurposeIn(menuIds, imagePurposes);
    }

    @Override
    public Optional<Image> findByMenuIdAndImagePurpose(Long menuId, ImagePurpose purpose){
        return imageJpaRepository.findByMenuIdAndImagePurpose(menuId,purpose);
    }
}
