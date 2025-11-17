package org.sopt.starbucks.domain.mymenu.api;

import java.util.List;

public record HomeMyMenuListResponse(
        List<HomeMyMenuResponse> myMenuList
) {
}
