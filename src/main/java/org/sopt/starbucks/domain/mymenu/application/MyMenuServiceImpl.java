package org.sopt.starbucks.domain.mymenu.application;

import lombok.RequiredArgsConstructor;
import org.sopt.starbucks.domain.image.application.ImageService;
import org.sopt.starbucks.domain.image.domain.Image;
import org.sopt.starbucks.domain.image.domain.ImagePurpose;
import org.sopt.starbucks.domain.menu.domain.Menu;
import org.sopt.starbucks.domain.mymenu.api.HomeMyMenuListResponse;
import org.sopt.starbucks.domain.mymenu.api.HomeMyMenuResponse;
import org.sopt.starbucks.domain.mymenu.api.PersonalMenuDetailResponse;
import org.sopt.starbucks.domain.mymenu.api.ListMyMenuListResponse;
import org.sopt.starbucks.domain.mymenu.api.ListMyMenuResponse;
import org.sopt.starbucks.domain.mymenu.domain.MyMenu;
import org.sopt.starbucks.domain.mymenu.domain.MyMenuRepository;
import org.sopt.starbucks.domain.mymenu.domain.Size;
import org.sopt.starbucks.global.exception.BusinessException;
import org.sopt.starbucks.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용
public class MyMenuServiceImpl implements MyMenuService {

    private final MyMenuRepository myMenuRepository;
    private final ImageService imageService;

    @Override
    public HomeMyMenuListResponse findHomeMyMenuList() {
        List<MyMenu> myMenuList = myMenuRepository.findAll();

        // 공통 메서드 호출 (HOME용 이미지 맵 생성)
        Map<Long, Map<ImagePurpose, String>> imageMap = createImageMap(
                myMenuList,
                List.of(ImagePurpose.HOME_ICE, ImagePurpose.HOME_HOT, ImagePurpose.HOME)
        );

        List<HomeMyMenuResponse> responses = myMenuList.stream()
                .map(myMenu -> {
                    // Purpose 로직 분리
                    ImagePurpose purpose = determineImagePurpose(myMenu, ImagePurpose.HOME);
                    String imageUrl = getImageUrl(imageMap, myMenu.getMenu().getId(), purpose);

                    // 옵션 문자열 생성 로직 분리
                    String optionSummary = buildOptionString(myMenu);

                    return HomeMyMenuResponse.of(
                            myMenu.getId(),
                            myMenu.getCustomName(),
                            optionSummary,
                            imageUrl
                    );
                })
                .toList();

        return new HomeMyMenuListResponse(responses);
    }

    @Override
    public ListMyMenuListResponse findListMyMenuList() {
        List<MyMenu> myMenuList = myMenuRepository.findAll();

        // 공통 메서드 호출 (MENU용 이미지 맵 생성)
        Map<Long, Map<ImagePurpose, String>> imageMap = createImageMap(
                myMenuList,
                List.of(ImagePurpose.MENU_ICE, ImagePurpose.MENU_HOT, ImagePurpose.MENU)
        );

        List<ListMyMenuResponse> responses = myMenuList.stream()
                .map(myMenu -> {
                    ImagePurpose purpose = determineImagePurpose(myMenu, ImagePurpose.MENU);
                    String imageUrl = getImageUrl(imageMap, myMenu.getMenu().getId(), purpose);

                    // 이름 접두사 (핫/아이스) 처리
                    String prefixName = getMenuPrefix(myMenu);
                    String optionSummary = buildOptionString(myMenu);

                    // 가격 계산
                    int totalPrice = myMenu.calculateTotalPrice();

                    return ListMyMenuResponse.of(
                            myMenu.getId(),
                            myMenu.getCustomName(),
                            prefixName + myMenu.getMenu().getMenuKr(),
                            optionSummary,
                            totalPrice,
                            imageUrl
                    );
                })
                .toList();

        return new ListMyMenuListResponse(responses);
    }

    // === 내부 헬퍼 메서드 ===

    // 이미지 맵 생성 공통 로직
    private Map<Long, Map<ImagePurpose, String>> createImageMap(List<MyMenu> menus, List<ImagePurpose> targetPurposes) {
        if (menus.isEmpty()) return Collections.emptyMap();

        List<Long> menuIds = menus.stream()
                .map(myMenu -> myMenu.getMenu().getId())
                .distinct() // 중복 ID 제거 최적화
                .toList();

        return imageService.findAllByMenuIdInAndImagePurposeIn(menuIds, targetPurposes).stream()
                .collect(Collectors.groupingBy(
                        img -> img.getMenu().getId(),
                        Collectors.toMap(Image::getImagePurpose, Image::getImageUrl, (a, b) -> a) // 중복 키 방어
                ));
    }

    // 이미지 Purpose 결정 로직
    private ImagePurpose determineImagePurpose(MyMenu myMenu, ImagePurpose defaultType) {

        boolean isHome = (defaultType == ImagePurpose.HOME);

        if (myMenu.getIsHot() == null) {
            return isHome ? ImagePurpose.HOME : ImagePurpose.MENU;
        }
        if (myMenu.getIsHot()) {
            return isHome ? ImagePurpose.HOME_HOT : ImagePurpose.MENU_HOT;
        }
        return isHome ? ImagePurpose.HOME_ICE : ImagePurpose.MENU_ICE;
    }

    // 이미지 URL 추출
    private String getImageUrl(Map<Long, Map<ImagePurpose, String>> map, Long menuId, ImagePurpose purpose) {
        return map.getOrDefault(menuId, Collections.emptyMap())
                .getOrDefault(purpose, ""); // 없으면 빈 문자열
    }

    // 옵션 문자열 생성
    private String buildOptionString(MyMenu myMenu) {
        StringBuilder sb = new StringBuilder();

        if (myMenu.getIsHot() != null) {
            sb.append(myMenu.getIsHot() ? "HOT | " : "ICED | ");
        }
        if (myMenu.getSize() != null) {
            sb.append(myMenu.getSize().getSize()).append(" | ");
        }
        sb.append(myMenu.getOptionsSummary());

        return sb.toString();
    }

    // 메뉴 이름 접두사
    private String getMenuPrefix(MyMenu myMenu) {
        if (myMenu.getIsHot() == null) return "";
        return myMenu.getIsHot() ? "핫 " : "아이스 ";
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
                Size.TALL.getSize(), Size.TALL.getPrice(),
                Size.GRANDE.getSize(), Size.GRANDE.getPrice(),
                Size.VENTI.getSize(),Size.VENTI.getPrice()
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