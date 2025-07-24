package org.example.cleanprj.services;

import org.example.cleanprj.models.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private final List<Booking> bookings;

    public BookingService() {
        this.bookings = new ArrayList<>();
    }

    public Booking createBooking(Tourist tourist, Guide guide, Destination destination, String notes) {
        String bookingId = UUID.randomUUID().toString();
        Booking booking = new Booking(
                bookingId,
                tourist,
                guide,
                destination,
                LocalDateTime.now(),
                "pending",
                notes
        );
        bookings.add(booking);
        return booking;
    }

    public List<Booking> getBookingsByTourist(Tourist tourist) {
        return bookings.stream()
                .filter(booking -> booking.getTourist().equals(tourist))
                .toList();
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public void updateBookingStatus(String bookingId, String status) {
        bookings.stream()
                .filter(booking -> booking.getId().equals(bookingId))
                .findFirst()
                .ifPresent(booking -> booking.setStatus(status));
    }
}