package org.sopt.starbucks.domain.mymenu.api;

import lombok.RequiredArgsConstructor;
import org.sopt.starbucks.domain.mymenu.application.MyMenuService;
import org.sopt.starbucks.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/my-menu")
@RestController
public class MyMenuController {

    private final MyMenuService myMenuService;

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<HomeMyMenuListResponse>> getHomeMyMenuList(){
        HomeMyMenuListResponse homeMyMenuList = myMenuService.findHomeMyMenuList();
        return ResponseEntity.ok(
                ApiResponse.ok(homeMyMenuList)
        );
    }
}
