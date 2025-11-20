package org.sopt.starbucks.domain.mymenu.api;

import lombok.RequiredArgsConstructor;
import org.sopt.starbucks.domain.mymenu.application.MyMenuService;
import org.sopt.starbucks.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/mymenu")
@RestController
public class MyMenuController {

    private final MyMenuService myMenuService;

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<HomeMyMenuListResponse>> getHomeMyMenuList() {
        HomeMyMenuListResponse homeMyMenuList = myMenuService.findHomeMyMenuList();
        return ResponseEntity.ok(
                ApiResponse.ok(homeMyMenuList)
        );
    }

    @GetMapping("/{my-menu-id}")
    public ResponseEntity<ApiResponse<PersonalMenuDetailResponse>> getPersonalMenuDetails(@PathVariable Long myMenuId) {
        PersonalMenuDetailResponse response = myMenuService.getPersonalMenuDetails(myMenuId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(response));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<?>> getListMyMenuList () {
        ListMyMenuListResponse listMyMenuList = myMenuService.findListMyMenuList();
        return ResponseEntity.ok(
                ApiResponse.ok(listMyMenuList)
            );
        }

    @PatchMapping("{my-menu-id}")
    public ResponseEntity<ApiResponse<PersonalMenuUpdateResponse>> updatePersonalMenuDetails(@PathVariable Long myMenuId, PersonalMenuUpdateRequest request){
        PersonalMenuUpdateResponse response = myMenuService.updatePersonalMenuDetails(myMenuId,request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(response));
    }
    }
