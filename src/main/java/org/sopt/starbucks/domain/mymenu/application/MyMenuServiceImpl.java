package org.sopt.starbucks.domain.mymenu.application;

import lombok.RequiredArgsConstructor;
import org.sopt.starbucks.domain.image.application.ImageService;
import org.sopt.starbucks.domain.image.domain.Image;
import org.sopt.starbucks.domain.image.domain.ImagePurpose;
import org.sopt.starbucks.domain.menu.domain.Menu;
import org.sopt.starbucks.domain.mymenu.api.*;
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

        String fullNameKr = getFullNameKr(myMenu,menu);
        String fullNameEng = getFullNameEng(myMenu,menu);

        // 내부 메서드 사용
        ImagePurpose imagePurpose = determineImagePurpose(myMenu, ImagePurpose.MENU_ICE);

        // 이미지 URL 조회
        String imageUrl = imageService.findByMenuIdAndImagePurpose(menu.getId(), imagePurpose)
                .map(Image::getImageUrl).orElse("");

        // 내부 메서드 사용
        Map<String, Integer> sizePrices = createSizePriceMap();

        // 옵션 요약 (온도/사이즈만)
        String summary = optionSummary(myMenu);

        return PersonalMenuDetailResponse.of(
                menu.getCategory().getName(),
                myMenu.getId(),
                fullNameKr,
                fullNameEng,
                menu.getInfo(),
                menu.getPrice(),
                myMenu.getCount(),
                myMenu.getIsHot(),
                myMenu.getSize(),
                sizePrices,
                myMenu.getPersonalOptions(),
                summary,
                imageUrl
        );

    }


    private String getFullNameKr(MyMenu myMenu, Menu menu) {
        if (myMenu.getIsHot() == null) {
            // 음식은 접두사 없음
            return menu.getMenuKr();
        } else if (myMenu.getIsHot()) {
            return "핫 " + menu.getMenuKr();
        } else {
            return "아이스 " + menu.getMenuKr();
        }
    }

    private String getFullNameEng(MyMenu myMenu, Menu menu) {
        if (myMenu.getIsHot() == null) {
            // 음식은 접두사 없음
            return menu.getMenuEng();
        } else if (myMenu.getIsHot()) {
            return "Hot " + menu.getMenuEng();
        } else {
            return "Iced " + menu.getMenuEng();
        }
    }

    // Size별 가격
    private Map<String, Integer> createSizePriceMap() {
        return Map.of(
                Size.TALL.getSize(), Size.TALL.getPrice(),
                Size.GRANDE.getSize(), Size.GRANDE.getPrice(),
                Size.VENTI.getSize(), Size.VENTI.getPrice()
        );
    }

    // 옵션 요약 (온도|사이즈) 만
    private String optionSummary(MyMenu myMenu) {
        StringBuilder sb = new StringBuilder();
        if (myMenu.getIsHot() != null) {
            sb.append(myMenu.getIsHot() ? "HOT | " : "ICED | ");
        }
        if(myMenu.getSize() != null) {
            sb.append(myMenu.getSize().getSize());
        }
        return sb.toString();
    }

    @Transactional
    public PersonalMenuUpdateResponse updatePersonalMenuDetails(Long myMenuId,PersonalMenuUpdateRequest request) {

        // 1. 나만의 메뉴 가져오기
        MyMenu mymenu = myMenuRepository.findById(myMenuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MYMENU));

        // 2. Hot,Ice에 따라 상태 저장하기

        if(request.isHot() != null){
            mymenu.updateIsHot(request.isHot());
        }

        // 3. 사이즈 저장하기
        if(request.size() != null) {
            mymenu.updateSize(request.size());
        }

        // 4. 요약 저장하기
        String summary = optionSummary(mymenu);

        // 5. 옵션 저장하기
        if(request.personalOptions() != null) {
            mymenu.updatePersonalOptions(request.personalOptions());
        }

        return PersonalMenuUpdateResponse.of(mymenu.getId(),
                mymenu.getIsHot(),
                mymenu.getSize(),
                summary,
                mymenu.getPersonalOptions());
    }
}