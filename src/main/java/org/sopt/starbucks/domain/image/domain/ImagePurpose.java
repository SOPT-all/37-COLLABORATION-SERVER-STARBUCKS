package org.sopt.starbucks.domain.image.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImagePurpose {
    HOME_HOT("HOME_HOT"), HOME_ICE("HOME_ICE"),
    MENU_HOT("MENU_HOT"), MENU_ICE("MENU_ICE"),

    // 음식용
    HOME("HOME"), MENU("MENU");

    private final String imagePurpose;
}
