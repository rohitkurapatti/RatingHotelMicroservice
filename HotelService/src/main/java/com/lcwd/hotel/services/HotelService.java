package com.lcwd.hotel.services;

import com.lcwd.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {

    //create a hote
    Hotel createHotel(Hotel hotel);

    //get A Hotel
    Hotel getHotel(String hotelId);

    //get all hotels
    List<Hotel> getAllHotels();


}
