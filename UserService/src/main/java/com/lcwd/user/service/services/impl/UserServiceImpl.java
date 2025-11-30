package com.lcwd.user.service.services.impl;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.externalservices.HotelService;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${service.url.ratingservice}")
    private String ratingURL;

/*    @Value("${service.url.hotelservice}")
    private String hotelURL;*/

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        log.debug("Saving user in DB");
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserOfId(String userId) {
        //Get user from database
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with given id : " + userId + " not found in database"));

        //Fetch rating from rating serivice, which is registered in eureka server
        Rating[] ratingsListOfUser = restTemplate.getForObject(ratingURL + user.getUserId(), Rating[].class);
        log.info("{}", ratingsListOfUser);

        List<Rating> ratings = Arrays.stream(ratingsListOfUser).toList();

        //Fetch the hotels for that particular user for which user has submitted the rating
        List<Rating> listOfRatings = ratings.stream().map(rating -> {

            //Fetch the hotels using api call through feign Client
//            System.out.println(hotelURL + rating.getRatingId());

            Hotel hotel = hotelService.getHotel(rating.getHotelId());

            rating.setHotel(hotel);

            System.out.println("Hotel - " + hotel);

            return rating;

        }).collect(Collectors.toList());

        user.setUserRatings(listOfRatings);

        return user;
    }

    @Override
    public String deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with given id : " + userId + " not found in database"));
        userRepository.deleteById(userId);
        return " User with userId " + userId + " has been deletedSuccessfully";

    }

    @Override
    public User updateUser(User modifyUser) {
        User user = userRepository.findById(modifyUser.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User with given id : " + modifyUser.getUserId() + " not found in database"));
        user.setName(modifyUser.getName());
        user.setEmail(modifyUser.getEmail());
        user.setAbout(modifyUser.getAbout());
        return userRepository.save(user);

    }
}
