package com.lcwd.hotel.exceptions;

import org.springframework.http.HttpStatus;

public class HotelWithSameDetailsExistsException extends RuntimeException {
    public HotelWithSameDetailsExistsException(HttpStatus httpStatus, String s) {
        super(s);
    }

    public HotelWithSameDetailsExistsException(){
        super("Hotel with same details exists");
    }
}
