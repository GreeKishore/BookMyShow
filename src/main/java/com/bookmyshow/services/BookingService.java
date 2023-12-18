package com.bookmyshow.services;

import com.bookmyshow.models.*;

import com.bookmyshow.repositories.BookingRepository;
import com.bookmyshow.repositories.ShowRepository;
import com.bookmyshow.repositories.ShowSeatRepository;
import com.bookmyshow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;
    private final PriceCalculatorService priceCalculatorService;

    @Autowired
    public BookingService(UserRepository userRepository, ShowRepository showRepository, ShowSeatRepository showSeatRepository, BookingRepository bookingRepository, PriceCalculatorService priceCalculatorService) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.bookingRepository = bookingRepository;
        this.priceCalculatorService = priceCalculatorService;
    }
    //Transaction created
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking bookMovie(List<Long> showSeatIds,Long userId,Long showId){

        /*      ...............Today we will take lock here....................
                1. get the user using userid
                2. get the show using show id
                ..................take lock.........................
                3. get the showseat object using showseatid
                4. check if all the seats are available
                5. if not available we should throw an exception
                6. if available we need to mark the seat staus as locked
                7. save the updated show seat in the db
                .................Release the lock........................
                8. Create a booking object
                9. return the booking object
                ...............Today we will release lock here....................
         */
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new RuntimeException("No user available - null pointer exception");
        }
        User bookedBy = userOptional.get();

        Optional<Show> showOptional = showRepository.findById(showId);
        if(showOptional.isEmpty()){
            throw new RuntimeException("No show available - null pointer exception");
        }
        Show bookedShow = showOptional.get();

        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatIds);
        for (ShowSeat showSeat: showSeats){
            if(!(isShowSeatAvailable(showSeat))){
                throw new RuntimeException("Some of the show seats are not available");
            }
            showSeat.setStatus(ShowSeatStatus.BLOCKED);
        }

        List<ShowSeat> updateShowStatus = showSeatRepository.saveAll(showSeats);

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setShowSeats(updateShowStatus);
        booking.setUser(bookedBy);
        booking.setBookedAt(new Date());
        booking.setShow(bookedShow);
        booking.setAmount(priceCalculatorService.calculatePrice(bookedShow,updateShowStatus));
        booking.setPayments(new ArrayList<>());
        return bookingRepository.save(booking);

    }

    private boolean isShowSeatAvailable(ShowSeat showSeat) {
        return showSeat.getStatus().equals(ShowSeatStatus.AVAILABLE) ||
                (showSeat.getStatus().equals(ShowSeatStatus.BLOCKED) &&
                        ChronoUnit.MINUTES.between(new Date().toInstant(), showSeat.getBlockedAt().toInstant()) > 15);
    }
}
