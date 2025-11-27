package org.sopt.starbucks.domain.mymenu.application;

import org.sopt.starbucks.domain.mymenu.api.*;
import org.springframework.transaction.annotation.Transactional;

public interface MyMenuService {

    HomeMyMenuListResponse findHomeMyMenuList();
    PersonalMenuDetailResponse getPersonalMenuDetails(Long menuId);

    ListMyMenuListResponse findListMyMenuList();

    PersonalMenuUpdateResponse updatePersonalMenuDetails(Long myMenuId, PersonalMenuUpdateRequest request);

    @Transactional
    void resetPersonalMenuDetails(Long myMenuId);
}
