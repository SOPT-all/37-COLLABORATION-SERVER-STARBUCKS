package org.sopt.starbucks.domain.mymenu.application;

import lombok.RequiredArgsConstructor;
import org.sopt.starbucks.domain.image.application.ImageService;
import org.sopt.starbucks.domain.image.domain.Image;
import org.sopt.starbucks.domain.image.domain.ImagePurpose;
import org.sopt.starbucks.domain.menu.domain.Menu;
import org.sopt.starbucks.domain.mymenu.api.HomeMyMenuListResponse;
import org.sopt.starbucks.domain.mymenu.api.HomeMyMenuResponse;
import org.sopt.starbucks.domain.mymenu.api.PersonalMenuDetailResponse;
import org.sopt.starbucks.domain.mymenu.domain.MyMenu;
import org.sopt.starbucks.domain.mymenu.domain.MyMenuRepository;
import org.sopt.starbucks.domain.mymenu.domain.Size;
import org.sopt.starbucks.global.exception.BusinessException;
import org.sopt.starbucks.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyMenuServiceImpl implements MyMenuService {

    private final MyMenuRepository myMenuRepository;
    private final ImageService imageService;

    @Override
    public HomeMyMenuListResponse findHomeMyMenuList() {
        List<MyMenu> myMenuList = myMenuRepository.findAll();

        // 조회된 메뉴 ID 목록
        List<Long> menuIds = myMenuList.stream()
                .map(myMenu -> myMenu.getMenu().getId())
                .toList();

        // 메뉴 ID 목록으로 HOME 화면 이미지들만 조회
        List<Image> images = imageService.findAllByMenuIdInAndImagePurposeIn(
                menuIds,
                List.of(ImagePurpose.HOME_ICE, ImagePurpose.HOME_HOT, ImagePurpose.HOME) // 홈 화면용만 타겟팅
        );

        // Map<메뉴ID, Map<purpose, url>> 구조로 검색 속도 향상
        Map<Long, Map<ImagePurpose, String>> imageMap = images.stream()
                .collect(Collectors.groupingBy(
                        img -> img.getMenu().getId(), // 1차 Key: 메뉴 ID
                        Collectors.toMap(             // 2차 Key: imagePurpose
                                Image::getImagePurpose,
                                Image::getImageUrl
                        )
                ));

        List<HomeMyMenuResponse> responses = myMenuList.stream()
                .map(myMenu -> {
                    Long menuId = myMenu.getMenu().getId();

                    ImagePurpose targetPurpose;

                    if (myMenu.getIsHot() == null) {
                        // 1. 핫/아이스 구분이 없으면 -> 음식(HOME) 이미지
                        targetPurpose = ImagePurpose.HOME;
                    } else if (myMenu.getIsHot()) {
                        // 2. true면 -> 핫(HOME_HOT)
                        targetPurpose = ImagePurpose.HOME_HOT;
                    } else {
                        // 3. false면 -> 아이스(HOME_ICE)
                        targetPurpose = ImagePurpose.HOME_ICE;
                    }

                    // Map에서 메뉴ID로 찾고 -> purpose로 찾기
                    String imageUrl = imageMap.getOrDefault(menuId, Collections.emptyMap())
                            .get(targetPurpose);

                    // 이미지 없으면 기본 이미지 처리
                    if (imageUrl == null) imageUrl = "";

                    return HomeMyMenuResponse.of(
                            myMenu.getId(),
                            myMenu.getCustomName(),
                            myMenu.getOptionsSummary(),
                            imageUrl
                    );
                })
                .toList();

        return new HomeMyMenuListResponse(responses);
    }

    @Override
    public PersonalMenuDetailResponse getPersonalMenuDetails(Long myMenuId) {

        // MyMenu 조회
        MyMenu myMenu = myMenuRepository.findById(myMenuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MYMENU));

        // MyMenu로부터 Menu 참조
        Menu menu = myMenu.getMenu();

        // Size별 가격
        Map<String,Integer> sizePrices = Map.of(
                "Tall", Size.TALL.getPrice(),
                "Grande",Size.GRANDE.getPrice(),
                "Venti",Size.VENTI.getPrice()
        );

        ImagePurpose imagePurpose;

        // ImagePurpose에 따라 가져오는 사진
        if(myMenu.getIsHot() == null) {
            imagePurpose = ImagePurpose.MENU;
        } else if(myMenu.getIsHot()) {
            imagePurpose = ImagePurpose.MENU_HOT;
        } else
            imagePurpose = ImagePurpose.MENU_ICE;

        String imageUrl=imageService.findByMenuIdAndImagePurpose(menu.getId(),imagePurpose)
                .map(Image::getImageUrl).orElse("");

        return PersonalMenuDetailResponse.of(
                menu.getCategory().getName(),
                myMenu.getId(),
                menu.getMenuKr(),
                menu.getMenuEng(),
                menu.getInfo(),
                menu.getPrice(),
                myMenu.getCount(),
                myMenu.getIsHot(),
                myMenu.getSize(),
                sizePrices,
                myMenu.getPersonalOptions(),
                imageUrl
        );

    }
}
