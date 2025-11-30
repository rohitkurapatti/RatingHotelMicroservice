package com.lcwd.user.service.services;

import com.lcwd.user.service.entities.User;

import java.util.List;

public interface UserService {

    //Create a user
    User saveUser(User user);

    //Return a user
    List<User> getAllUsers();

    //Get User of Id
    User getUserOfId(String userId);

    //Delete a user
    String deleteUser(String userId);

    //Update a user
    User updateUser(User modifyUser);

}
