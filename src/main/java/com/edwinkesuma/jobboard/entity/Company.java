package com.edwinkesuma.jobboard.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private JobBoardUser user;

    private String companyName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String website;

    private String location;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "company")
    private List<Job> jobs;
}
