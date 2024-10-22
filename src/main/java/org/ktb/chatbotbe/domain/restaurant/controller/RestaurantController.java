package org.ktb.chatbotbe.domain.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.restaurant.dto.response.RestaurantResponse;
import org.ktb.chatbotbe.domain.restaurant.service.usecase.RestaurantQueryUserCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantQueryUserCase restaurantQueryUserCase;

    /**
    * 오늘의 추천 레스토랑 가져오기
    * */
    @GetMapping("")
    public ResponseEntity<List<RestaurantResponse>> getTodayRestaurant(){
        return ResponseEntity.ok(restaurantQueryUserCase.getTodayRecommendRestaurant());
    }
}
