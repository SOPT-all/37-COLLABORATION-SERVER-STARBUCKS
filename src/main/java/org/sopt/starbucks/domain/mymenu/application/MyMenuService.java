package org.sopt.starbucks.domain.mymenu.application;

import org.sopt.starbucks.domain.mymenu.api.*;

public interface MyMenuService {

    HomeMyMenuListResponse findHomeMyMenuList();
    PersonalMenuDetailResponse getPersonalMenuDetails(Long menuId);

    ListMyMenuListResponse findListMyMenuList();

    PersonalMenuUpdateResponse updatePersonalMenuDetails(Long myMenuId, PersonalMenuUpdateRequest request);
}
