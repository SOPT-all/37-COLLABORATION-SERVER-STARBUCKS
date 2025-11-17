package org.sopt.starbucks.domain.mymenu.api;

import org.sopt.starbucks.domain.mymenu.domain.PersonalOption;
import org.sopt.starbucks.domain.mymenu.domain.Size;

import java.util.List;
import java.util.Map;

public record PersonalMenuDetailResponse(
        String categoryName,
        Long myMenuId,
        String menuKr,
        String menuEng,
        String info,
        int price,
        int count,
        Boolean isHot,
        Size size,
        Map<String,Integer> sizePrices,
        List<PersonalOption> personalOptions,
        String menuImageUrl
) {
    public static PersonalMenuDetailResponse of(String categoryName, Long myMenuId, String menuKr,String menuEng, String info, int price,int count, Boolean isHot,
                                                Size size,Map<String,Integer> sizePrices, List<PersonalOption> personalOptions,String menuImageUrl)
    {
        return new PersonalMenuDetailResponse(categoryName, myMenuId, menuKr,menuEng, info, price, count, isHot, size, sizePrices, personalOptions, menuImageUrl);
    }
}
