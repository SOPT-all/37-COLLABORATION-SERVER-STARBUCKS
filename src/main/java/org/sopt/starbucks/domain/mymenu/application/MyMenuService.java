package org.sopt.starbucks.domain.mymenu.application;

import org.sopt.starbucks.domain.mymenu.api.HomeMyMenuListResponse;
import org.sopt.starbucks.domain.mymenu.api.PersonalMenuDetailResponse;
import org.sopt.starbucks.domain.mymenu.api.ListMyMenuListResponse;

public interface MyMenuService {

    HomeMyMenuListResponse findHomeMyMenuList();
    PersonalMenuDetailResponse getPersonalMenuDetails(Long menuId);

    ListMyMenuListResponse findListMyMenuList();
}
