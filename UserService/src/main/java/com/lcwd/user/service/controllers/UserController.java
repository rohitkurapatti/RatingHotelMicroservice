package com.lcwd.user.service.controllers;


import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    int retryCount = 1;


    @Autowired
    private UserService userService;

    //Create a user
    @PostMapping(path = "/createuser")
    public ResponseEntity<User> createNewUser(@RequestBody User user){
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    //Get a user
    @GetMapping("/{userId}")
    @Retry(name = "ratingHotelRetryService", fallbackMethod = "ratingHotelBreaker")
    @CircuitBreaker(name = "ratingHotelFallback",
            fallbackMethod = "ratingHotelBreaker")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelBreaker")
    public ResponseEntity<User> getUser(@PathVariable String userId){
        retryCount++;
        log.info("Retry count : {} ", retryCount);
        User userOfId = userService.getUserOfId(userId);
        return ResponseEntity.ok(userOfId);
    }

    public ResponseEntity<User> ratingHotelBreaker(String userId, Throwable ex){

//        log.info("Fallback executed because service is down for " + userId);
         User user = User.builder()
                .userId("1")
                .email("dummy@email.com")
                .about("This is a dummy message, since one of the services is failing")
                .userRatings(null)
                 .build();
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    //Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }

    //Delete a user

    //Update a user


}
