package com.edwinkesuma.jobboard.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer salaryMin;

    private Integer salaryMax;

    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

}