package org.ktb.chatbotbe.domain.restaurant.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RestaurantResponse(
        String name,
        String kakaoMapUrl,
        String recommendedBy,
        String recommendation,
        List<String> images
) {
}
