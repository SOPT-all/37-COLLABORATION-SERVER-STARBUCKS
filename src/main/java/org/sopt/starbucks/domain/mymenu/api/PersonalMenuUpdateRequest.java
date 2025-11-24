package org.sopt.starbucks.domain.mymenu.api;

import org.sopt.starbucks.domain.mymenu.domain.PersonalOption;
import org.sopt.starbucks.domain.mymenu.domain.Size;

import java.util.List;

public record PersonalMenuUpdateRequest(
        Boolean isHot,
        Size size,
        List<PersonalOption> personalOptions) {

}
