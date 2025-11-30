package com.lcwd.rating.services;

import com.lcwd.rating.entities.Rating;

import java.util.List;

public interface RatingService {

    //add a rating
    Rating createRating(Rating rating);

    //get all ratings
    List<Rating> getAllRatings();

    //get all rating by a user
    List<Rating> getAllRatingsByUser(String userId);

    //get all ratings by a hotel
    List<Rating> getAllRatingByHotel(String hotelId);
}
