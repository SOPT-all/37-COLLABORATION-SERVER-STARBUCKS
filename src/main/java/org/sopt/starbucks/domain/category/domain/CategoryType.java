package org.sopt.starbucks.domain.category.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    DRINK("DRINK"), FOOD("FOOD");

    private final String type;
}
