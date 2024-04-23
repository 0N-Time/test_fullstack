package com.example.backend.model.dao;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int round;

    @OneToOne
    private Account playerOne;

    @OneToOne
    private Account playerTwo;

}
