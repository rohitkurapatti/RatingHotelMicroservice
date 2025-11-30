package com.lcwd.user.service.entities;


import lombok.*;

//This is a different service, comes from different endpoint
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    private String ratingId;
    private String userId;
    private String hotelId;
    private int rating;
    private String feedback;

    private Hotel hotel;

}
