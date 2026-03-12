# JobBoard Backend

JobBoard Backend is a RESTful API for a job portal platform where companies can post job openings and job seekers can apply for them.

This project is built using **Spring Boot** and **MySQL**, providing authentication, job management, and application management APIs.

## Features

- User registration and login
- Role-based access (Job Seeker / Company)
- Company profile management
- Job posting management
- Apply to jobs
- View job applicants
- RESTful API architecture
- JWT Authentication

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- MySQL
- Maven

## Database Schema

Main entities:

- Users
- Companies
- Jobs
- Applications

Relationships:

- One-to-One: User → Company
- One-to-Many: Company → Jobs
- Many-to-Many: Users ↔ Jobs (via Applications)

## API Endpoints

### Authentication
