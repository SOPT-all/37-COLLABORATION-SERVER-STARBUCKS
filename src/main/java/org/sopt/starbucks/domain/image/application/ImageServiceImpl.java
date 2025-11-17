package org.sopt.starbucks.domain.image.application;

import lombok.RequiredArgsConstructor;
import org.sopt.starbucks.domain.image.domain.Image;
import org.sopt.starbucks.domain.image.domain.ImagePurpose;
import org.sopt.starbucks.domain.image.domain.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    private final ImageRepository imageRepository;

    @Override
    public Image getImage(ImagePurpose imagePurpose, Long menuId) {
        return imageRepository.findByImagePurposeAndMenuId(imagePurpose, menuId).orElseThrow();
    }

    @Override
    public List<Image> findAllByMenuIdInAndImagePurposeIn(List<Long> menuIds, List<ImagePurpose> imagePurposes) {
        return imageRepository.findAllByMenuIdInAndImagePurposeIn(menuIds, imagePurposes);
    }
}
