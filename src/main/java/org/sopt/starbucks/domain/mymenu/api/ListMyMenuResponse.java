package org.sopt.starbucks.domain.mymenu.api;

public record ListMyMenuResponse(
        Long myMenuId,
        String myMenuName,
        String menuName,
        String myMenuOption,
        int price,
        String myMenuImage
) {
    public static ListMyMenuResponse of(Long myMenuId, String myMenuName, String menuName, String myMenuOption, int price, String myMenuImage) {
        return new ListMyMenuResponse(myMenuId, myMenuName, menuName, myMenuOption, price, myMenuImage);
    }
}
