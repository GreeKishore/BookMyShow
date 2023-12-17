package com.bookmyshow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Booking extends BaseModel{
    @Enumerated(value = EnumType.ORDINAL)
    private BookingStatus bookingStatus;
    @ManyToMany
    private List<ShowSeat> showSeats;
    @ManyToOne
    private User user;
    private Date bookedAt;
    @ManyToOne
    private MovieShow movieShow;
    private int amount;
    @OneToMany
    private List<Payment> payments;
}
