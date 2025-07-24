package org.example.cleanprj.models;

import java.time.LocalDateTime;

public class Booking {
    private String id;
    private Tourist tourist;
    private Guide guide;
    private Destination destination;
    private LocalDateTime bookingDate;
    private String status;
    private String notes;

    public Booking(String id, Tourist tourist, Guide guide, Destination destination,
                   LocalDateTime bookingDate, String status, String notes) {
        this.id = id;
        this.tourist = tourist;
        this.guide = guide;
        this.destination = destination;
        this.bookingDate = bookingDate;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Tourist getTourist() { return tourist; }
    public void setTourist(Tourist tourist) { this.tourist = tourist; }

    public Guide getGuide() { return guide; }
    public void setGuide(Guide guide) { this.guide = guide; }

    public Destination getDestination() { return destination; }
    public void setDestination(Destination destination) { this.destination = destination; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}