package org.sopt.starbucks.domain.mymenu.api;

import org.sopt.starbucks.domain.mymenu.domain.PersonalOption;
import org.sopt.starbucks.domain.mymenu.domain.Size;

import java.util.List;

public record PersonalMenuUpdateResponse(
        Long myMenuId,
        Boolean isHot,
        Size size,
        String summary,
        List<PersonalOption> personalOptions
) {
    public static PersonalMenuUpdateResponse of(
            Long myMenuId,
            Boolean isHot,
            Size size,
            String summary,
            List<PersonalOption> personalOptions
    ) {
        return new PersonalMenuUpdateResponse(myMenuId,isHot,size,summary,personalOptions);
    }
}
