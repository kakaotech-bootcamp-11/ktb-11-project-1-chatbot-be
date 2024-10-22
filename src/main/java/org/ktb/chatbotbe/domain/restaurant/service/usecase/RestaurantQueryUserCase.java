package org.ktb.chatbotbe.domain.restaurant.service.usecase;


import org.ktb.chatbotbe.domain.restaurant.dto.response.RestaurantResponse;

import java.util.List;

public interface RestaurantQueryUserCase {
    List<RestaurantResponse> getTodayRecommendRestaurant();

    List<RestaurantResponse> getTodayRestaurant();

}
