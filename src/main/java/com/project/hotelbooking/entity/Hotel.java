package com.project.hotelbooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.hotelbooking.entity.HotelContactInfo;
import com.project.hotelbooking.entity.Room;
import com.project.hotelbooking.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    @Column(columnDefinition = "TEXT[]")
    private String[] photos;

    @Column(columnDefinition = "TEXT[]")
    private String[] amenities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Embedded
    private HotelContactInfo contactInfo;

    @Column(nullable = false)
    private Boolean active;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User owner;

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<Room> rooms;

}