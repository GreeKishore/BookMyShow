package com.bookmyshow.controller;

import com.bookmyshow.dto.BookMovieRequestDto;
import com.bookmyshow.dto.BookMovieResponseDto;
import com.bookmyshow.dto.ResponseStatus;
import com.bookmyshow.models.Booking;
import com.bookmyshow.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public BookMovieResponseDto bookMovie(BookMovieRequestDto request){
        BookMovieResponseDto bookMovieResponseDto = new BookMovieResponseDto();

        try {
            Booking booking = bookingService.bookMovie(request.getShowSeatIds(), request.getUserId(), request.getShowId());
            bookMovieResponseDto.setBookingId(booking.getId());
            bookMovieResponseDto.setAmount(booking.getAmount());
            bookMovieResponseDto.setStatus(ResponseStatus.SUCCESS);
        }catch (Exception e){
            bookMovieResponseDto.setStatus(ResponseStatus.FAILURE);
        }
        return bookMovieResponseDto;
    }
}
