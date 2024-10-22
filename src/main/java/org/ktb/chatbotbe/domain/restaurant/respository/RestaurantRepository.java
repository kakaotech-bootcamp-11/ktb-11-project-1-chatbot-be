package org.ktb.chatbotbe.domain.restaurant.respository;

import org.ktb.chatbotbe.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query(value = "SELECT * FROM restaurant ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Restaurant> findRandomRestaurant(@Param("limit") int limit);
}
