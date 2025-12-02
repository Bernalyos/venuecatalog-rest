CREATE TABLE venues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    location VARCHAR(255),
    capacity INT
);

CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    date DATETIME,
    venue_id BIGINT,
    CONSTRAINT fk_venue FOREIGN KEY (venue_id) REFERENCES venues(id)
);
