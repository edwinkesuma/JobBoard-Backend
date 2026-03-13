CREATE TABLE companies
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    company_name VARCHAR(150) NOT NULL,
    description  TEXT,
    website      VARCHAR(255),
    location     VARCHAR(150),
    logo_url     VARCHAR(255),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);