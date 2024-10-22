package org.ktb.chatbotbe.domain.restaurant.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ktb.chatbotbe.domain.restaurant.dto.response.RestaurantResponse;
import org.ktb.chatbotbe.domain.restaurant.entity.DailyRecommendation;
import org.ktb.chatbotbe.domain.restaurant.entity.Restaurant;
import org.ktb.chatbotbe.domain.restaurant.respository.DailyRecommendationRepository;
import org.ktb.chatbotbe.domain.restaurant.respository.RestaurantRepository;
import org.ktb.chatbotbe.domain.restaurant.service.usecase.RestaurantQueryUserCase;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService implements RestaurantQueryUserCase {
    private final RestaurantRepository restaurantRepository;
    private final DailyRecommendationRepository dailyRecommendationRepository;

    @Override
    @Transactional
    public List<RestaurantResponse> getTodayRecommendRestaurant() {
        LocalDate today = LocalDate.now();
        List<DailyRecommendation> recommendations = dailyRecommendationRepository.findByDate(today);

        if (!recommendations.isEmpty()) {
            List<Restaurant> restaurants = recommendations.stream()
                    .map(DailyRecommendation::getRestaurant)
                    .toList();
            return getRecommendResponse(restaurants);
        }

        List<Restaurant> restaurants = createNewRecommendations();

        return getRecommendResponse(restaurants);
    }

    @Override
    public List<RestaurantResponse> getTodayRestaurant() {
        RestaurantResponse.builder().build();
        return List.of();
    }

    private List<Restaurant> createNewRecommendations() {
        List<DailyRecommendation> recommendations;
        List<Restaurant> restaurants = restaurantRepository.findRandomRestaurant(3);
        recommendations = restaurants.stream()
                .map(restaurant -> DailyRecommendation.builder()
                        .restaurant(restaurant)
                        .build())
                .collect(Collectors.toList());

        dailyRecommendationRepository.saveAll(recommendations);
        return restaurants;
    }


    private List<RestaurantResponse> getRecommendResponse(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(request -> RestaurantResponse.builder()
                        .name(request.getName())
                        .kakaoMapUrl(request.getKakaoMapUrl())
                        .recommendedBy(request.getRecommendedBy())
                        .recommendation(request.getRecommendation())
                        .images(request.getRestaurantImageName())
                        .build()
                ).toList();
    }
}
