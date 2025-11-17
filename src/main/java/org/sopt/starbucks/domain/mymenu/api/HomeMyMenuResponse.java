package org.sopt.starbucks.domain.mymenu.api;

public record HomeMyMenuResponse(
        Long myMenuId,
        String myMenuName,
        String myMenuOption,
        String myMenuImage
) {

    public static HomeMyMenuResponse of(Long myMenuId, String myMenuName, String myMenuOption, String myMenuImage){
        return new HomeMyMenuResponse(myMenuId, myMenuName, myMenuOption, myMenuImage);
    }
}
