CREATE TABLE jobs
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id  BIGINT       NOT NULL,
    title       VARCHAR(150) NOT NULL,
    description TEXT         NOT NULL,
    salary_min  INT,
    salary_max  INT,
    location    VARCHAR(150),
    job_type    ENUM('FULL_TIME','PART_TIME','REMOTE','INTERNSHIP'),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE CASCADE
);