package org.sopt.starbucks.domain.mymenu.application;

import org.sopt.starbucks.domain.mymenu.api.HomeMyMenuListResponse;
import org.sopt.starbucks.domain.mymenu.api.PersonalMenuDetailResponse;

public interface MyMenuService {

    HomeMyMenuListResponse findHomeMyMenuList();
    PersonalMenuDetailResponse getPersonalMenuDetails(Long menuId);
}
