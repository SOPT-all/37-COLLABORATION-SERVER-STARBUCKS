package org.sopt.starbucks.domain.mymenu.api;

import org.sopt.starbucks.domain.mymenu.domain.PersonalOption;
import org.sopt.starbucks.domain.mymenu.domain.Size;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public record PersonalMenuDetailResponse(
        String categoryName,
        Long myMenuId,
        String hotMenuKr,
        String hotMenuEng,
        String hotMenuImageUrl,
        String iceMenuKr,
        String iceMenuEng,
        String iceMenuImageUrl,
        String info,
        int price,
        int count,
        Boolean isHot,
        Size size,
        Map<String, Integer> sizePrices,
        List<PersonalOption> personalOptions,
        String summary
) {
    public static PersonalMenuDetailResponse of(
            String categoryName,
            Long myMenuId,
            String hotMenuKr,
            String hotMenuEng,
            String hotMenuImageUrl,
            String iceMenuKr,
            String iceMenuEng,
            String iceMenuImageUrl,
            String info,
            int price,
            int count,
            Boolean isHot,
            Size size,
            Map<String, Integer> sizePrices,
            List<PersonalOption> personalOptions,
            String summary
    ) {
        return new PersonalMenuDetailResponse(categoryName,
                myMenuId,
                hotMenuKr,
                hotMenuEng,
                hotMenuImageUrl,
                iceMenuKr,
                iceMenuEng,
                iceMenuImageUrl,
                info,
                price,
                count,
                isHot,
                size,
                sizePrices,
                personalOptions,
                summary);
    }
}
