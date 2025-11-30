package com.lcwd.hotel.services.implementation;

import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.exceptions.HotelWithSameDetailsExistsException;
import com.lcwd.hotel.exceptions.ResourceNotFoundException;
import com.lcwd.hotel.repositories.HotelRepository;
import com.lcwd.hotel.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {

        boolean exists = hotelRepository.existsByNameAndLocationAndAbout(
                hotel.getName(),
                hotel.getLocation(),
                hotel.getAbout()
        );

        if (exists) {
            throw new HotelWithSameDetailsExistsException(HttpStatus.CONFLICT,
                    "Hotel already exists with the same name, location, and description");
        }
        String hotelId = UUID.randomUUID().toString();
        hotel.setId(hotelId);
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel getHotel(String hotelId) {
        return hotelRepository.findById(hotelId).orElseThrow(() ->
                new ResourceNotFoundException("Hotel with hotel id : " + hotelId + " is not found in database"));
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
}
