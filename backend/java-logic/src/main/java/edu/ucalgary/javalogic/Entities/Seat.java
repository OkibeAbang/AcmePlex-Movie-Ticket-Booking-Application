package edu.ucalgary.javalogic.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "seat", nullable = false)
    private String seat;

    @Column(name = "is_reserved")
    private Boolean isReserved;

    @Column(name = "showtime_id")
    private Long showtime_id;

    public Seat() {}

    public Seat(String seat, Boolean isReserved, Long showtime_id) {
        this.seat = seat;
        this.isReserved = isReserved;
        this.showtime_id = showtime_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public Boolean getIsReserved() {
        return isReserved;
    }

    public void setIsReserved(Boolean isReserved) {
        this.isReserved = isReserved;
    }

    public Long getShowtime_id(){return this.showtime_id;}

    public void setShowtime_id(Long showtime_id){this.showtime_id = showtime_id;}
}