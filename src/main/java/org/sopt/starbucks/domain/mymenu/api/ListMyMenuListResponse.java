package org.sopt.starbucks.domain.mymenu.api;

import java.util.List;

public record ListMyMenuListResponse(
        List<ListMyMenuResponse> myMenuList
) {
}
