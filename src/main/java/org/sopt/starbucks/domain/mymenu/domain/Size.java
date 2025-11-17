package org.sopt.starbucks.domain.mymenu.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Size {
    TALL("Tall", 0), GRANDE("Grande", 800), VENTI("Venti", 1600);

    private final String size;
    private final int price;
}
